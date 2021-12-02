package com.asapp.backend.challenge.unit;

import static com.asapp.backend.challenge.util.TestConnectionUtils.get;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.asapp.backend.challenge.repository.UserRepository;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.util.TestSchemaRunner;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UserRepositoryTests {

   @Order(1)
   @Test
   @SneakyThrows
   @DisplayName("User Repository :: Create :: OK")
   void check_user_creation_is_OK() {

      final var connection = get();

      Assertions.assertThat(connection).isNotNull();

      final var userRepository = new UserRepository(connection);
      final var userResource = UserResource.builder().username("test").password("test").build();

      final var userResponse = userRepository.create(userResource);
      Assertions.assertThat(userResponse).isNotNull().extracting("id").isNotEqualTo(0);
   }

   @Order(2)
   @Test
   @SneakyThrows
   @DisplayName("User Repository :: Find Username :: OK")
   void check_username_exists() {

      var connection = get();
      Assertions.assertThat(connection).isNotNull();

      var userRepository = new UserRepository(connection);
      final var userResource = UserResource.builder().username("test").password("test").build();
      final var userResponse = userRepository.create(userResource);

      Assertions.assertThat(userResponse).isNotNull();

      userRepository = new UserRepository(get());
      final var found = userRepository.findByUsername("test");

      Assertions.assertThat(found).isNotNull().matches(usr -> usr.getId() == found.getId() && usr.getUsername().equals("test"));
   }

   @SneakyThrows
   @BeforeAll
   static void setup() {
      TestSchemaRunner.run(get());
   }
}
