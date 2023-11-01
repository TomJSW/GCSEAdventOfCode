package org.swainston.ui.homepage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.swainston.AuthenticatedWebPage;
import org.swainston.ui.challengepage.ChallengePage;

/**
 * As this page does not implement {@link AuthenticatedWebPage} it does not require auth to access
 */
public class HomePage extends WebPage {
  private static final long serialVersionUID = 1L;

  /**
   * Method containing the scripts required to run the home page
   *
   * @param parameters page parameters required for execution
   */
  public HomePage(final PageParameters parameters) {
    super(parameters);

    // Response page for the hyperlink "Go to challenges"
    add(new Link<Void>("challengePageLink") {
      public void onClick() {
        setResponsePage(new ChallengePage());
      }
    });

    // Response page for the navbar sign in button
    add(new Link<Void>("signIn") {
      public void onClick() {
        setResponsePage(new ChallengePage());
      }
    });
  }
}

