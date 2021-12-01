package com.asapp.backend.challenge.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.asapp.backend.challenge.utils.config.ConfigParser;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
public enum ConnectionFactory {
   INSTANCE;

   public Connection get() throws SQLException {
      final var url = ConfigParser.getDataSourceUrl();
      final var connection = DriverManager.getConnection(url);
      connection.setAutoCommit(false);

      return connection;
   }
}
