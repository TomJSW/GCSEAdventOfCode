package org.swainston.ui.signin;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.swainston.SignInSession;
import org.swainston.ui.homepage.HomePage;
import org.swainston.ui.signup.SignUpPage;

import java.util.HashMap;
import java.util.Map;

/**
 * Class containing scripting for the sign in page
 */
public class SignInPage extends WebPage {
    private static final long serialVersionUID = 1L;

    /**
     * Constructing method that is run to load the active components of the page
     * @param parameters page parameters, required for super class
     */
    public SignInPage(final PageParameters parameters) {
        super(parameters);

        // Form used for taking users credentials
        add(new SignInForm("signInForm"));

        // Component that links users to create a login
        add(new Link<Void>("signUpLink") {
            public void onClick() {
                setResponsePage(SignUpPage.class);
            }
        });

        // Navigation bar home button
        add(new Link<Void>("go-home") {
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });

        // REDUNDANT, REDIRECTS TO HOMEPAGE, SAME AS ABOVE+
        add(new Link<Void>("sign-out") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });
    }

    /**
     * Class required to create the form that takes the users credentials
     */
    private static final class SignInForm extends Form<Void> {
        private static final String USERNAME = "username";
        private static final String PASSWORD = "password";

        private final Map<String, String> properties = new HashMap<>();

        /**
         * Method that creates and handles the data given within the form
         * @param id the wicket:id of the HTML component
         */
        public SignInForm(final String id) {
            super(id);

            add(new TextField<>(USERNAME, new PropertyModel<String>(properties, USERNAME)));
            add(new PasswordTextField(PASSWORD, new PropertyModel<>(properties, PASSWORD)));
            add(new Button("signInFormSubmit"));
        }

        @Override
        public void onSubmit() {

            // Get session info for the user
            var signInSession = getMySession();

            // Sign the user in and proceed to the original page that was the destination before
            // flow was interrupted by this login action.
            if (signInSession.signIn(getUsername(), getPassword())) {
                continueToOriginalDestination();
            } else {
                // Get the error message from the properties file associated with the Component
                String loginError = getString("loginError", null, "Sign in details not recognised.");

                // Register the error message with the feedback panel
                error(loginError);
            }
        }

        private String getPassword() {
            return properties.get(PASSWORD);
        }

        private String getUsername() {
            return properties.get(USERNAME);
        }

        private SignInSession getMySession() {
            return (SignInSession) getSession();
        }
    }
}

