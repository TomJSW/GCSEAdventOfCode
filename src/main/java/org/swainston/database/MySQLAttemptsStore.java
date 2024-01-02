package org.swainston.database;

import org.swainston.Attempt;
import org.swainston.AttemptsStore;
import org.swainston.Challenge;
import org.swainston.WicketApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Holds the method that inserts a users default information into the attempts table.
 */
public class MySQLAttemptsStore implements AttemptsStore {

  private static final String SET_ATTEMPT =
      "UPDATE attempts SET attempt = ?" + " WHERE email = ? AND id = ?";
  private static final String LOAD_DEFAULT = "INSERT INTO attempts VALUES(?,?,?)";
  private static final String GET_ATTEMPT = "SELECT * FROM attempts WHERE email = ? AND id = ?";

  /**
   * Loads a default template into the attempt table.
   *
   * @param email     users email address
   * @param challenge the challenge
   */
  public static void loadDefault(String email, Challenge challenge) {

    try (Connection conn = WicketApplication.getConnection()) {

      PreparedStatement stmt = conn.prepareStatement(LOAD_DEFAULT);
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
   * @param email the student's email
   * @param id    the id of the attempt
   *
   * @return Optional Attempt
   */
  @Override
  public Optional<Attempt> getAttempt(String email, int id) {


    try (Connection conn = WicketApplication.getConnection();
         PreparedStatement stmt = conn.prepareStatement(GET_ATTEMPT)) {
      stmt.setString(1, email);
      stmt.setInt(2, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        Attempt attempt = new Attempt();
        attempt.setEmail(rs.getString(1));
        attempt.setId(rs.getInt(2));
        attempt.setAttempt(rs.getString(3));

        return Optional.of(attempt);

      }
      return Optional.empty();
    } catch (SQLException e) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds a students attempt to the database
   *
   * @param email   the student's email
   * @param id      the id of the attempt
   * @param attempt the students attempt at the challenge
   */
  @Override
  public void setAttempt(String email, int id, String attempt) {

    try (Connection conn = WicketApplication.getConnection()) {

      PreparedStatement stmt = conn.prepareStatement(SET_ATTEMPT);
      stmt.setString(2, email);
      stmt.setInt(3, id);
      stmt.setString(1, attempt);
      stmt.executeUpdate();

    } catch (SQLException e) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
    }
  }
}

