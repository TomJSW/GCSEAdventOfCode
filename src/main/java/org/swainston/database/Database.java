package org.swainston.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Holds the method that makes a connection with the database.
 */
public class Database {

  /**
   * Initializes the database source with the server name, port number, database name, user and
   * password.
   *
   * @return DataSource the source from the
   *
   * @throws SQLException if unable to connect to database
   */
  public static DataSource initDataSource() throws SQLException {

    MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();

    dataSource.setServerName("localhost");
    dataSource.setPortNumber(3306);
    dataSource.setDatabaseName("GCSEAdventOfCode");
    // Database user username
    dataSource.setUser("root");
    // Database user password
    dataSource.setPassword("Password123!");

    try (Connection conn = dataSource.getConnection()) {
      if (!conn.isValid(1)) {
        throw new SQLException("Failed to establish database connection.");
      }
    }

    return dataSource;
  }
}
