package org.swainston;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.csp.CSPDirective;
import org.apache.wicket.csp.CSPDirectiveSrcValue;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.swainston.database.Database;
import org.swainston.ui.HomePage;
import org.swainston.ui.SignInPage;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 *
 */
public class WicketApplication extends WebApplication {

  private static DataSource DATASOURCE;

  public static Connection getConnection() throws SQLException {
    return DATASOURCE.getConnection();
  }

  /**
   * @see org.apache.wicket.Application#getHomePage()
   */
  @Override
  public Class<? extends WebPage> getHomePage() {
    return HomePage.class;
  }

  @Override
  public Session newSession(Request request, Response response) {
    return new SignInSession(request);
  }

  /**
   * @see org.apache.wicket.Application#init()
   */
  @Override
  public void init() {

    try {
      DATASOURCE = Database.initDataSource();
      initDB();
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }

    super.init();

    // needed for the styling used by the quickstart
    getCspSettings().blocking()
        .add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF)
        .add(CSPDirective.STYLE_SRC, "https://fonts.googleapis.com/css")
        .add(CSPDirective.STYLE_SRC,
            // custom icons on navbar
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css")
        .add(CSPDirective.FONT_SRC,
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/fonts/fontawesome-webfont.woff2")
        .add(CSPDirective.FONT_SRC,
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/fonts/fontawesome-webfont.woff")
        .add(CSPDirective.FONT_SRC,
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/fonts/fontawesome-webfont.ttf");


    // Register the authorization strategy
    getSecuritySettings().setAuthorizationStrategy(
        new IAuthorizationStrategy.AllowAllAuthorizationStrategy() {
          @Override
          public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
              Class<T> componentClass) {
            // Check if the new Page requires authentication (implements the marker interface)
            if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
              if (((SignInSession) Session.get()).isSignedIn()) {

                return true;

              }
              // Not logged in. Redirect to the login page but keep track of where we were heading so
              // can can resume the flow after logging in. This is done by calling
              // continueToOriginalDestination when logged in.
              throw new RestartResponseAtInterceptPageException(SignInPage.class);
            }
            // Page we're being directed to does not require authentication.
            return true;
          }
        });
  }


  private void initDB() throws SQLException, IOException {
    String setup;
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("setupdb.sql")) {
      setup = new String(in.readAllBytes());
    } catch (IOException e) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
      return;
    }
    String[] queries = setup.split(";");
    for (String query : queries) {
      try (Connection conn = DATASOURCE.getConnection();
           PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.execute();
      }
    }
    System.out.println("Database loaded & ready!");
  }
}
