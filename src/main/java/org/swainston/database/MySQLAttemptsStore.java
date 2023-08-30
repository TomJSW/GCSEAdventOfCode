package org.swainston.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.swainston.Attempt;
import org.swainston.AttemptsStore;
import org.swainston.Challenge;
import org.swainston.WicketApplication;

/**
 * Holds the method that inserts a users default information into the attempts table.
 */
public class MySQLAttemptsStore implements AttemptsStore {

  /**
   * Loads a default template into the attempts table.
   *
   * @param email users email address
   * @param challenge the challenge
   */
  public static void loadDefault(String email, Challenge challenge) {
    String query = "INSERT INTO attempts VALUES(?,?,?)";

    try (Connection conn = WicketApplication.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, email);
      stmt.setInt(2, challenge.getId());
      stmt.setString(3, challenge.getSolutionTemplate());
      stmt.executeUpdate();

    } catch (SQLException e) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
    }

  }


  /**
   * Gets an attempt.
   *
   * @param email the students email
   * @param id    the id of the attempt
   * @return Optional of attempt
   */
  @Override
  public Optional<Attempt> getAttempt(String email, int id) {

    String query = "SELECT * FROM attempts WHERE email = ? AND id = ?";

    try (Connection conn = WicketApplication.getConnection();
         PreparedStatement stmt =
             conn.prepareStatement(query)) {
      stmt.setString(1, email);
      stmt.setInt(2, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Attempt attempt = new Attempt();
        //TODO - add validity checks
        attempt.setEmail(rs.getString(1));
        attempt.setId(rs.getInt(2));
        attempt.setAttempt(rs.getString(3));

        return Optional.of(attempt);

      }
      return Optional.empty();
    } catch (SQLException e) {
      // TODO - should this be WicketApplication.class or current class?
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setAttempt(String email, int id, String attempt) {
    String query = "UPDATE attempts SET attempt = ? WHERE email = ? AND id = ?";

    try (Connection conn = WicketApplication.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(2, email);
      stmt.setInt(3, id);
      stmt.setString(1, attempt);
      stmt.executeUpdate();

    } catch (SQLException e) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
    }
  }
}

