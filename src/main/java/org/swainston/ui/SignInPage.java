package org.swainston.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.value.ValueMap;
import org.swainston.SignInSession;

public class SignInPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public SignInPage(final PageParameters parameters) {
        super(parameters);
        add(new SignInForm("signInForm"));
        add(new FeedbackPanel("feedback"));
        add(new Link<Void>("signUpLink") {
            public void onClick() {
                setResponsePage(SignUpPage.class);
            }
        });

        add(new Link<String>("go-home", (IModel) () -> "foo") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });
        add(new Link<String>("sign-out", (IModel) () -> "foo") {
            @Override
            public void onClick() {
//                getMySession().signOut();
                setResponsePage(HomePage.class);
            }
        });
        // TODO Add your page's components here
    }

    public final class SignInForm extends Form<Void> {
        private static final String USERNAME = "username";
        private static final String PASSWORD = "password";

        private final ValueMap properties = new ValueMap();

        public SignInForm(final String id) {
            super(id);

            add(new TextField<>(USERNAME, new PropertyModel<String>(properties, USERNAME)));
            add(new PasswordTextField(PASSWORD, new PropertyModel<>(properties, PASSWORD)));
            add(new Button("signInFormSubmit"));
        }

        @Override
        public final void onSubmit() {

            // Get session info for the user
            SignInSession session = getMySession();

            // Sign the user in and proceed to the original page that was the destination before
            // flow was interrupted by this login action.
            if (session.signIn(getUsername(), getPassword())) {
                continueToOriginalDestination();
            } else {
                // Get the error message from the properties file associated with the Component
                String errmsg = getString("loginError", null, "Sign in details not recognised.");

                // Register the error message with the feedback panel
                error(errmsg);
            }
        }

        private String getPassword() {
            return properties.getString(PASSWORD);
        }

        private String getUsername() {
            return properties.getString(USERNAME);
        }

        private SignInSession getMySession() {
            return (SignInSession) getSession();
        }
    }
}

