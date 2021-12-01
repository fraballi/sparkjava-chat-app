package com.asapp.backend.challenge.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.exception.ApiException;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
public interface BaseRepository {

   default int lastId(final Connection connection, final String table) throws ApiException {

      final var sql = "select last_insert_rowid() as last_id from " + table;
      try (var statement = connection.createStatement()) {
         final var lastId = statement.executeQuery(sql);
         return lastId.getInt("last_id");

      } catch (SQLException e) {
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error getting latest row id from table: " + table);
      }
   }

   default int closestId(final Connection connection, final String table, final Integer start) throws ApiException {

      if (Objects.isNull(start) || start == 0) {
         return 1;
      }

      final var sql = "select max(rowid) as closest_id from " + table + " where rowid <= :start group by rowid";
      try (var statement = connection.prepareStatement(sql)) {
         statement.setInt(1, start);
         final var result = statement.executeQuery();

         if (result.next()) {
            return result.getInt("closest_id");
         }

         return 1;

      } catch (SQLException e) {
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error getting closest row id from table: " + table);
      }
   }
}
