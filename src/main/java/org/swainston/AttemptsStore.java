package org.swainston;

import java.util.Optional;

public interface AttemptsStore {

  Optional<Attempt> getAttempt(String user, int id);

  void setAttempt(String email, int id, String attempt);

}
