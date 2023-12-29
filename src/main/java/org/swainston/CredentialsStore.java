package org.swainston;

public interface CredentialsStore {
  boolean add(String email, String password);

  boolean exists(String email);

  boolean validate(String email, String password);
}

