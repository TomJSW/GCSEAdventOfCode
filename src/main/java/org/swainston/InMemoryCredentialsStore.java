package org.swainston;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is only used for local testing prior to the implementation of a database.
 *
 * @see org.swainston.database.MySQLCredentialsStore
 */
public class InMemoryCredentialsStore implements CredentialsStore {

  private final Map<String, String> credentialMap = new HashMap<>();

  @Override
  public boolean add(String email, String password) {
    if (!exists(email)) {
      credentialMap.put(email, password);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean exists(String email) {
    return credentialMap.containsKey(email);
  }

  @Override
  public boolean validate(String email, String password) {
    return (credentialMap.containsKey(email) && credentialMap.get(email).equals(password));
  }
}
