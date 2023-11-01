package org.swainston.ui.signup;

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
import org.swainston.ui.homepage.HomePage;

import java.util.regex.Pattern;

public class SignUpPage extends WebPage {
  private static final long serialVersionUID = 1L;

  public SignUpPage(final PageParameters parameters) {
    super(parameters);
    add(new SignUpForm("signUpForm"));
    add(new FeedbackPanel("feedback"));

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

  }

  public static final class SignUpForm extends Form<Void> {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private final ValueMap properties = new ValueMap();

    public SignUpForm(final String id) {
      super(id);

      // Attach textfield components that edit properties map model
      add(new TextField<>(USERNAME, new PropertyModel<String>(properties, USERNAME)));
      add(new PasswordTextField(PASSWORD, new PropertyModel<>(properties, PASSWORD)));
      add(new Button("signUpFormSubmit"));
    }

    @Override
    public void onSubmit() {
      Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9_]+$"); // Check not all underscores?
      Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"); // Check not all underscores?

      String username = getUsername();
      if (!pattern.matcher(username).matches()) {
        // Get the error message from the properties file associated with the Component
        String errmsg = getString("loginErrorInvalidUsername", null,
            "Email address invalid.");
        error(errmsg);
        return;
      }

      String password = getPassword();
      if (!passwordPattern.matcher(password).matches()) {
        // Get the error message from the properties file associated with the Component
        String errmsg = getString("loginErrorInvalidPassword", null,
            "Only letters, number, underscore in password");
        error(errmsg);
        return;
      }

      // Sign up, and return to the original destination page.
      SignInSession session = getMySession();
      if (session.signUp(username, password)) {
        System.out.println("Executing if");
        // Sign the user in
        if (session.signIn(username, password)) {
          continueToOriginalDestination();
        } else {
          // Get the error message from the properties file associated with the Component
          String errmsg = getString("loginError", null, "Sign in details not recognised.");

          // Register the error message with the feedback panel
          error(errmsg);
        }
      } else {
        System.out.println("Executing else");
        // Get the error message from the properties file associated with the Component
        String errmsg;
        if (session.userExists(getUsername())) {
          errmsg = getString("loginError", null,
              "Unable to sign you up - that user name already exists");
        } else {
          errmsg = getString("loginError", null, "Unable to sign you up");
        }
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

