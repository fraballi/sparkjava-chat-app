package com.asapp.backend.challenge.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.exception.ApiException;

import lombok.extern.slf4j.Slf4j;
import spark.utils.IOUtils;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
public final class TestSchemaRunner {

   public static void run(final Connection connection) throws ApiException {

      final var classLoader = Thread.currentThread().getContextClassLoader();
      final var schemaResource = classLoader.getResourceAsStream("schema-test.sql");

      try {

         final var sql = IOUtils.toString(Objects.requireNonNull(schemaResource));
         final var batch = sql.replace(System.lineSeparator(), "").split(";");

         for (final var command : batch) {
            try (var statement = connection.prepareStatement(command)) {
               statement.executeUpdate();
            }
         }

         connection.commit();

         log.info("### Database schema initialized ###");

      } catch (SQLException | IOException e) {
         try {
            log.info("### Error: Cannot create Database schema ###");
            if (Objects.nonNull(connection)) {
               connection.rollback();
            }
         } catch (SQLException ex) {
            log.warn(e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error creating database schema");
         }
      } finally {
         try {
            if (Objects.nonNull(connection)) {
               connection.close();
            }
         } catch (SQLException e) {
            log.warn(e.getMessage());
         }
      }
   }
}
