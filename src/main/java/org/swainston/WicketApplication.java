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
import org.swainston.ui.homepage.HomePage;
import org.swainston.ui.signin.SignInPage;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application object for your web application. If you want to run this application without
 * deploying, run the Start class.
 */
public class WicketApplication extends WebApplication {
  /**
   * @see org.apache.wicket.Application#getHomePage()
   */
  @Override
  public Class<? extends WebPage> getHomePage() {
    return HomePage.class;
  }

  private static DataSource DATASOURCE;

  public static Connection getConnection() throws SQLException {
    return DATASOURCE.getConnection();
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

    // Required to allow custom fonts to be displayed and not be blocked by Content Security Policy
    getCspSettings().blocking()
        .add(CSPDirective.STYLE_SRC, CSPDirectiveSrcValue.SELF)
        // Google fonts
        .add(CSPDirective.STYLE_SRC, "https://fonts.googleapis.com/css")
        // FontAwesome fonts stylesheet
        .add(CSPDirective.STYLE_SRC,
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css")
        // FontAwesome custom font
        .add(CSPDirective.FONT_SRC,
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/fonts/fontawesome-webfont.woff2")
        .add(CSPDirective.FONT_SRC,
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/fonts/fontawesome-webfont.woff")
        .add(CSPDirective.FONT_SRC,
            "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/fonts/fontawesome-webfont.ttf");

     //   .add(CSPDirective.SCRIPT_SRC, "https://kit.fontawesome.com/79cfa1bf9a.js");



    // Register the authorization strategy
    getSecuritySettings().setAuthorizationStrategy(
        new IAuthorizationStrategy.AllowAllAuthorizationStrategy() {
          @Override
          public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
              Class<T> componentClass) {
            // Check if the page requires auth, if it does it will implement marker class, and checks
            // if the session is signed in
            if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
              if (((SignInSession) Session.get()).isSignedIn()) {
                return true;
              }
              // Session not signed in and page requires auth. Send them to auth, then redirect
              //  to the original page
              // continueToOriginalDestination when logged in.
              throw new RestartResponseAtInterceptPageException(SignInPage.class);
            }
            // The Page we're being directed to does not require authentication.
            return true;
          }
        });
  }


  /**
   * Initialises the database and uses the <a href="setUpDB.sql">setUpDB.sql</a> to populate the
   * database.
   *
   * @throws SQLException if prepared statement fails to execute.
   * @throws IOException  if content within the SQL file is invalid.
   */
  private void initDB() throws SQLException, IOException {
    String setup;
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("setUpDB.sql")) {
      if (inputStream != null) {
        setup = new String(inputStream.readAllBytes());
      } else {
        throw new AssertionError("Failed to read bytes of resource.");
      }
    } catch (IOException ex) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, ex.getMessage());
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


