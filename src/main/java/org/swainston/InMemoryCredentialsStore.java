package org.swainston;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCredentialsStore implements CredentialsStore {

  private final Map<String, String> map = new HashMap<>();

  @Override
  public boolean add(String email, String password) {
    if (!exists(email)) {
      map.put(email, password);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean exists(String email) {
    return map.containsKey(email);
  }

  @Override
  public boolean validate(String email, String password) {
    return (map.containsKey(email) && map.get(email).equals(password));
  }
}
