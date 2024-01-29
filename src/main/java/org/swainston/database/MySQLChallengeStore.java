package org.swainston.database;

import org.swainston.Challenge;
import org.swainston.ChallengeStore;
import org.swainston.WicketApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MySQLChallengeStore implements ChallengeStore {

  private static final String GET_NEXT_CHALLENGE =
      "SELECT * FROM challenges WHERE id = (SELECT progress FROM users where email = ?)";


  /**
   * Fetches the student's next challenge and returns it as an optional
   * challenge
   * @param userEmail the user's email
   * @return optional of challenge
   */
  public Optional<Challenge> getNextChallenge(String userEmail) {

    try (Connection conn = WicketApplication.getConnection();
         PreparedStatement stmt = conn.prepareStatement(GET_NEXT_CHALLENGE)) {
      stmt.setString(1, userEmail);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        Challenge challenge = new Challenge();
        challenge.setId(resultSet.getInt(1));
        challenge.setTitle(resultSet.getString(2));
        challenge.setDescription(resultSet.getString(3));
        challenge.setSolutionTemplate(resultSet.getString(4));

        return Optional.of(challenge);

      }
      return Optional.empty();
    } catch (SQLException e) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
      throw new RuntimeException(e);

    }
  }
}
