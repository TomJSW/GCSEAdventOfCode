package org.swainston.database;

import org.swainston.CredentialsStore;
import org.swainston.WicketApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mysql thing.
 */
public class MySQLCredentialsStore implements CredentialsStore {

  private static final String SQL_ADD = "INSERT INTO users VALUES(?, ?, ?)";
  private static final String SQL_EXISTS = "SELECT COUNT(*) FROM users WHERE email = ?;";
  private static final String SQL_VALIDATE =
      "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?;";

  @Override
  public boolean add(String email, String password) {
    if (exists(email)) {
      return false;
    }
    try (Connection conn = WicketApplication.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(SQL_ADD);
      stmt.setString(1, email);
      stmt.setString(2, password);
      stmt.setInt(3, 1);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      Logger.getLogger(WicketApplication.class.getName()).log(Level.SEVERE, e.getMessage());
    }
    return false;
  }

  @Override
  public boolean exists(String email) {

    try (var conn = WicketApplication.getConnection();
         var stmt = conn.prepareStatement(SQL_EXISTS)) {
      stmt.setString(1, email);
      var resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        if (resultSet.getInt(1) == 1) {
          return true;
        } else if (resultSet.getInt(1) > 1) {
          throw new SQLDataException("Duplicate user found");
        }
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean validate(String email, String password) {
    try (var conn = WicketApplication.getConnection();
         var stmt = conn.prepareStatement(SQL_VALIDATE)) {
      stmt.setString(1, email);
      stmt.setString(2, password);
      var resultSet = stmt.executeQuery();

      while (resultSet.next()) {
        if (resultSet.getInt(1) >= 1) {
          return true;
        }
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
