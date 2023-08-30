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

  public static final String QUERY =
      "SELECT * FROM challenges WHERE id = (SELECT progress FROM users where email = ?)";


  public Optional<Challenge> getNextChallenge(String user) {

    try (Connection conn = WicketApplication.getConnection();
         PreparedStatement stmt =
             conn.prepareStatement(QUERY)) {
      stmt.setString(1, user);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        Challenge challenge = new Challenge();
        //TODO - add validity checks
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
