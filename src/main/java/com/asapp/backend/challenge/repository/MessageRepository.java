package com.asapp.backend.challenge.repository;

import static com.asapp.backend.challenge.utils.dto.MessageFullResponse.Content;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.utils.dto.MessageFullResponse;
import com.asapp.backend.challenge.utils.dto.MessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
@RequiredArgsConstructor
public class MessageRepository implements BaseRepository {

   private final Connection connection;

   private static final int DEFAULT_LIMIT = 100;

   public Collection<MessageFullResponse> paginate(final Integer recipient, Integer start, Integer limit) throws ApiException {

      final var messages = new ArrayList<MessageFullResponse>();
      if (Objects.isNull(recipient)) {
         return messages;
      }

      start = closestId(connection, "MessageResource", start);

      if (Objects.isNull(limit) || limit == 0) {
         limit = DEFAULT_LIMIT;
      }

      final var sql =
            "select mr.id, mr.sender, mr.recipient, mr.timestamp, c.type, c.text from MessageResource mr left join Content c on mr.id = c.messageId "
                  + "where mr.recipient = :recipient and mr.id >= :start order by mr.id limit :limit";

      try (var statement = connection.prepareStatement(sql)) {
         statement.setInt(1, recipient);
         statement.setInt(2, start);
         statement.setInt(3, limit);

         final var foundRow = statement.executeQuery();

         while (foundRow.next()) {
            final var id = foundRow.getInt("id");
            final var sender = foundRow.getInt("sender");
            final var recipient1 = foundRow.getInt("recipient");
            final var timestamp = foundRow.getString("timestamp");
            final var type = foundRow.getString("type");
            final var text = foundRow.getString("text");

            final var response = MessageFullResponse
                  .builder()
                  .id(id)
                  .sender(sender)
                  .recipient(recipient1)
                  .timestamp(timestamp)
                  .content(Content.builder().type(type).text(text).build())
                  .build();
            messages.add(response);
         }

      } catch (SQLException e) {
         log.warn(e.getMessage());
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error listing messages by recipient");

      } finally {
         try {
            connection.close();
         } catch (SQLException e) {
            log.warn(e.getMessage());
         }
      }

      return Collections.unmodifiableCollection(messages);
   }

   public MessageResponse create(final MessageResource messageResource) throws ApiException {

      final var builder = MessageResponse.builder();
      try {

         createMessage(messageResource);

         final var lastMessageId = lastId(connection, "MessageResource");

         createContent(messageResource.getContent(), lastMessageId);

         connection.commit();

         return builder.id(lastMessageId).build();

      } catch (Exception e) {
         try {
            log.warn(e.getMessage());
            connection.rollback();

         } catch (SQLException ex) {
            log.warn(e.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, e.getMessage());
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

   private void createMessage(final MessageResource messageResource) throws ApiException {

      final var createMessage = "INSERT INTO MessageResource (rowid, sender, recipient, timestamp) VALUES (:id, :sender, :recipient, :timestamp)";
      try (final var statement = connection.prepareStatement(createMessage)) {
         statement.setInt(2, messageResource.getSender());
         statement.setInt(3, messageResource.getRecipient());
         statement.setString(4, messageResource.getTimestamp());
         statement.executeUpdate();

      } catch (SQLException e) {
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error creating a new user");
      }
   }

   private void createContent(final MessageResource.@NotNull Content content, final int lastMessageId) throws ApiException {

      final var createContent = "INSERT INTO Content (rowid, messageId, type, text) VALUES (:id, :messageId, :type, :text)";
      try (final var statement1 = connection.prepareStatement(createContent)) {
         statement1.setInt(2, lastMessageId);
         statement1.setString(3, content.getType().name());
         statement1.setString(4, content.getText());
         statement1.executeUpdate();

      } catch (SQLException e) {
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, "Error creating new message content");
      }
   }
}
