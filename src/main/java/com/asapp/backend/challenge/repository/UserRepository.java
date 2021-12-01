package com.asapp.backend.challenge.repository;

import java.sql.Connection;
import java.sql.SQLException;

import org.eclipse.jetty.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.utils.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
@RequiredArgsConstructor
public class UserRepository implements BaseRepository {

   private final Connection connection;

   private static final int HASH_ROUNDS = 10;

   public UserResource findByUsername(final String username) throws ApiException {

      final var builder = UserResource.builder();

      final var sql = "select * from UserResource ur where ur.username = :username";
      try (var statement = connection.prepareStatement(sql)) {
         statement.setString(1, username);
         final var foundRow = statement.executeQuery();

         if (foundRow.next()) {
            final var id = foundRow.getInt("id");
            final var username1 = foundRow.getString("username");
            final var password = foundRow.getString("password");

            return builder.id(id).username(username1).password(password).build();
         }

      } catch (SQLException e) {
         log.warn(e.getMessage());
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error searching by username");

      } finally {
         try {
            connection.close();
         } catch (SQLException e) {
            log.warn(e.getMessage());
         }
      }
      return builder.build();
   }

   public UserResponse create(final UserResource userDTO) throws ApiException {

      final var builder = UserResponse.builder();

      final var createQuery = "INSERT INTO UserResource (rowid, username, password) VALUES (:id, :username, :password)";
      try (final var statement = connection.prepareStatement(createQuery)) {
         statement.setString(2, userDTO.getUsername());

         final var password = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(HASH_ROUNDS));
         statement.setString(3, password);
         statement.executeUpdate();

         connection.commit();

         final var lastId = lastId(connection, "UserResource");
         return builder.id(lastId).build();

      } catch (SQLException e) {
         try {
            log.warn(e.getMessage());
            connection.rollback();
         } catch (SQLException ex) {
            log.warn(e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error creating a new user");
         }
      } finally {
         try {
            connection.close();
         } catch (SQLException e) {
            log.warn(e.getMessage());
         }
      }

      return builder.build();
   }
}
