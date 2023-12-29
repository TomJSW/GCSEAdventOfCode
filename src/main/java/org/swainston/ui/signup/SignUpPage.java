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

/**
 * <p>Contains the script for the sign-up HTML page.
 * <br><br>
 * Does not implement {@link org.swainston.AuthenticatedWebPage} as this page does not
 * require authentication to be viewed.</p>
 *
 * @see org.apache.wicket.markup.html.WebPage
 */
public class SignUpPage extends WebPage {

  private static final long serialVersionUID = 1L;

  /**
   * Constructing method that initliaises the HTML components
   *
   * @param parameters page parameters for super class
   */
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

  /**
   * The subclass that houses the code to handle the form used for the sign-up
   * @see org.apache.wicket.markup.html.form.Form
   * @see org.apache.wicket.util.value.ValueMap
   */
  private static final class SignUpForm extends Form<Void> {

    // Constants required to populate value map
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final ValueMap properties = new ValueMap();

    /**
     * Constructing method that initialises the components of the form
     * @param id form id
     */
    public SignUpForm(final String id) {
      super(id);

      //Username text field
      add(new TextField<>(USERNAME, new PropertyModel<String>(properties, USERNAME)));

      // Password text field
      add(new PasswordTextField(PASSWORD, new PropertyModel<>(properties, PASSWORD)));

      // Sign up button
      add(new Button("signUpFormSubmit"));
    }

    /**
     * <p>Submit method that is executed when the sign-up button is pressed <br>
     *  Handles all validation required for the user accounts </p>
     */
    @Override
    public void onSubmit() {

      // Validation for the email
      Pattern emailPattern = Pattern.compile(
          "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

      // Validation for the password using regex
      Pattern passwordPattern = Pattern.compile("^[a-zA-Z0-9_]+$");

      String username = getUsername();

      if (!emailPattern.matcher(username).matches()) {
        // Get the error message from the properties file associated with the Component
        String loginErrorInvalidUsername = getString("loginErrorInvalidUsername", null,
            "Email address invalid.");
        error(loginErrorInvalidUsername);
        return;
      }

      String password = getPassword();

      if (!passwordPattern.matcher(password).matches()) {
        // Get the error message from the properties file associated with the Component
        String invalidPassword = getString("loginErrorInvalidPassword", null,
            "Only letters, number, underscore in password");
        error(invalidPassword);
        return; // break to ensure that no unnecessary session are created
      }

      // Sign up, and return to the original destination page.
      SignInSession signInSession = getMySession();

      // Check if the sign-up is successful
      if (signInSession.signUp(username, password)) {

        // Sign the user in
        if (signInSession.signIn(username, password)) {
          continueToOriginalDestination();
        } else {
          // Get the error message from the properties file associated with the Component
          String loginError = getString("loginError", null, "Sign in details not recognised.");

          // Register the error message with the feedback panel
          error(loginError);
        }
      } else {
        // Get the error message from the properties file associated with the Component
        String errorMessage;
        if (signInSession.userExists(getUsername())) {
          errorMessage = getString("loginError", null,
              "Unable to sign you up - that user name already exists");
        } else {
          errorMessage = getString("loginError", null, "Unable to sign you up");
        }
        // Register the error message with the feedback panel
        error(errorMessage);
      }
    }

    /**
     *
     * @return password from properties ValueMap
     */
    private String getPassword() {
      return properties.getString(PASSWORD);
    }

    /**
     *
     * @return username from properties ValueMap
     */
    private String getUsername() {
      return properties.getString(USERNAME);
    }

    /**
     *
     * @return Apache Wicket session cast to type {@link SignInSession}
     */
    private SignInSession getMySession() {
      return (SignInSession) getSession();
    }
  }
}

