package com.asapp.backend.challenge.unit;

import static com.asapp.backend.challenge.util.TestConnectionUtils.get;
import static com.asapp.backend.challenge.util.TestConstants.DEFAULT_RECIPIENT_ID;
import static com.asapp.backend.challenge.util.TestConstants.DEFAULT_SENDER_ID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.asapp.backend.challenge.repository.MessageRepository;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.resources.MessageResource.Content;
import com.asapp.backend.challenge.util.TestSchemaRunner;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class MessageRepositoryTests {

   @Order(1)
   @Test
   @SneakyThrows
   @DisplayName("Message Repository :: Creation :: OK")
   void check_message_creation_is_OK() {

      final var connection = get();

      Assertions.assertThat(connection).isNotNull();

      final var content = new Content();
      content.setText("Text Content");
      content.setType(Content.Type.TEXT);

      final var messageResource = MessageResource.builder().sender(DEFAULT_SENDER_ID).recipient(DEFAULT_RECIPIENT_ID).content(content).build();

      final var messageRepository = new MessageRepository(connection);
      final var messageResponse = messageRepository.create(messageResource);

      Assertions.assertThat(messageResponse).isNotNull().extracting("id").isNotEqualTo(0);
   }

   @Order(2)
   @Test
   @SneakyThrows
   @DisplayName("Message Repository :: Pagination :: OK")
   void check_message_pagination_is_OK() {

      final var connection = get();

      Assertions.assertThat(connection).isNotNull();

      var messageRepository = new MessageRepository(connection);
      final var responses = messageRepository.paginate(DEFAULT_RECIPIENT_ID, 0, 0);

      Assertions.assertThat(responses).isNotEmpty();
   }

   @SneakyThrows
   @BeforeAll
   static void setup() {
      TestSchemaRunner.run(get());
   }
}
