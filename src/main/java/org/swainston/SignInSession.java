package org.swainston;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.swainston.database.MySQLCredentialsStore;

/**
 * Used for verifying credentials for sessions
 */
public final class SignInSession extends AuthenticatedWebSession {

  // Credential store is set to desired store
  private final CredentialsStore credentialsStore = new MySQLCredentialsStore();

  private String username;

  /**
   * Constructing method that is run when the class is called
   * @param request the request object (as per apache javadoc)
   */
  public SignInSession(Request request) {
    super(request);
    // Credentials that always exist to allow for testing
    if (!credentialsStore.exists("foo")) {
      credentialsStore.add("foo", "bar");
    }
  }

  /**
   * Trivial sign out method
   */
  @Override
  public void signOut() {
    super.signOut();
  }

  /**
   * Authenticates the given username and password.
   *
   * @param username the username
   * @param password the password
   *
   * @return true if the user was authenticated
   */
  @Override
  public boolean authenticate(final String username, final String password) {
    if (credentialsStore.validate(username, password)) {
      this.username = username;
    } else {
      this.username = null;
    }
    return this.username != null;
  }

  /**
   * Validates whether a user exists
   *
   * @param username the username
   *
   * @return whether the user exists
   */
  public boolean userExists(String username) {
    return credentialsStore.exists(username);
  }

  /**
   * Creates a new user in the credential store
   *
   * @param username the validated username
   * @param password the password
   *
   * @return whether the sign-up was a success
   */
  public boolean signUp(final String username, final String password) {
    if (!userExists(username)) {
      return credentialsStore.add(username, password);
    } else {
      return false;
    }
  }

  /**
   *  Getter method for the username attribute
   * @return the username
   */
  public String getUser() {
    return username;
  }

  /**
   * @param user New user
   */
  public void setUser(final String user) {
    this.username = user;
  }

  @Override
  public Roles getRoles() {
    return null;
  }
}