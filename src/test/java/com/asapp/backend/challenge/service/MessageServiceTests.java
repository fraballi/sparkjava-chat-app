package com.asapp.backend.challenge.service;

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
import com.asapp.backend.challenge.util.TestSchemaRunner;

import lombok.SneakyThrows;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class MessageServiceTests {

   @Order(1)
   @Test
   @SneakyThrows
   @DisplayName("Message Service :: Creation :: OK")
   void check_message_service_creation_is_OK() {

      final var content = new MessageResource.Content();
      content.setText("Text Content");
      content.setType(MessageResource.Content.Type.TEXT);

      final var messageResource = MessageResource.builder().sender(DEFAULT_SENDER_ID).recipient(DEFAULT_RECIPIENT_ID).content(content).build();

      final var messageRepository = new MessageRepository(get());
      final var messageService = new MessageService(messageRepository);
      final var messageResponse = messageService.create(messageResource);

      Assertions.assertThat(messageResponse).isNotNull().extracting("id").isNotEqualTo(0);
   }

   @Order(2)
   @Test
   @SneakyThrows
   @DisplayName("Message Service :: Pagination :: OK")
   void check_message_service_pagination_is_OK() {

      var messageRepository = new MessageRepository(get());
      final var messageService = new MessageService(messageRepository);

      final var responses = messageService.getAll(DEFAULT_RECIPIENT_ID, 0, 0);

      Assertions.assertThat(responses).isNotEmpty();
   }

   @SneakyThrows
   @BeforeAll
   static void setup() {
      TestSchemaRunner.run(get());
   }
}
