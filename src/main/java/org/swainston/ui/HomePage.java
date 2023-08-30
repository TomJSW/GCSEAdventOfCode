package org.swainston.ui;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.swainston.AuthenticatedWebPage;
import org.swainston.ui.ChallengePage;

/**
 * Note that the home page is not password protected - it does not implement {@link AuthenticatedWebPage}.
 */
public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        add(new Link<Void>("challengePageLink") {
            public void onClick() {
                setResponsePage(new ChallengePage());
            }
        });

        // TODO Add your page's components here
    }

}

