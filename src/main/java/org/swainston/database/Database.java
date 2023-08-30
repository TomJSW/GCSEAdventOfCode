package org.swainston.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Holds the method that makes a connection with the database.
 */
public class Database {

  /**
   * Initializes the database source with the
   * server name, port number, database name, user and password.
   *
   * @return DataSource
   * @throws SQLException unable to connect
   */
  public static DataSource initDataSource() throws SQLException {
    MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
    dataSource.setServerName("localhost");
    dataSource.setPortNumber(3306);
    dataSource.setDatabaseName("GCSEAdventOfCode");
    dataSource.setUser("root");
    dataSource.setPassword("Password123!");
    try (Connection conn = dataSource.getConnection()) {
      if (!conn.isValid(1)) {
        throw new SQLException("Could not establish database connection.");
      }
    }
    return dataSource;
  }
}
