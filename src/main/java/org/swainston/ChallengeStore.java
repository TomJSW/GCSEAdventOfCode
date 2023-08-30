package org.swainston;

import java.util.Optional;

public interface ChallengeStore {

  Optional<Challenge> getNextChallenge(String user);

}
