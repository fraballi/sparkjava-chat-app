package com.asapp.backend.challenge.unit;

import static com.asapp.backend.challenge.util.TestConnectionUtils.get;

import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sqlite.jdbc4.JDBC4Connection;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
class DatabaseUnitTests {

   @Test
   @SneakyThrows
   @DisplayName("JDBC Connection :: OK")
   void check_database_connection_is_OK() {

      final var connection = get();

      final var url = "jdbc:sqlite:chat.test.db";

      Assertions
            .assertThat(connection)
            .isNotNull()
            .matches(conn -> ((JDBC4Connection) conn).getDatabase().getUrl().equals(url), "Invalid Connection URL")
            .matches(conn -> {
               try {
                  return conn.isValid(1000);
               } catch (SQLException e) {
                  log.debug(e.getMessage());
               }
               return false;
            }, "Invalid JDBC Connection");
   }
}
