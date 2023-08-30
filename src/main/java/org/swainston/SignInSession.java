package org.swainston;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.swainston.database.MySQLCredentialsStore;

public final class SignInSession extends AuthenticatedWebSession {
  private final CredentialsStore credentialsStore = new MySQLCredentialsStore();
  /**
   * Trivial user representation
   */
  private String user;

  SignInSession(Request request) {
    super(request);
    if (!credentialsStore.exists("foo")) {
      credentialsStore.add("foo", "bar");
    }
  }

  @Override
  public void signOut() {
    super.signOut();

  }

  /**
   * Authenticates the given username and password.
   *
   * @param username The username
   * @param password The password
   * @return True if the user was authenticated
   */
  @Override
  public boolean authenticate(final String username, final String password) {

    if (credentialsStore.validate(username, password)) {
      user = username;
    } else {
      user = null;
    }

    return user != null;
  }

  public boolean userExists(String username) {
    return credentialsStore.exists(username);
  }

  public boolean signUp(final String username, final String password) {
    if (!userExists(username)) {
      return credentialsStore.add(username, password);
    } else {
      return false;
    }
  }

  public String getUser() {
    return user;
  }

  /**
   * @param user New user
   */
  public void setUser(final String user) {
    this.user = user;
  }

  @Override
  public Roles getRoles() {
    return null;
  }
}