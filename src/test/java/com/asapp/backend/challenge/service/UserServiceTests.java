package com.asapp.backend.challenge.service;

import static com.asapp.backend.challenge.util.TestConnectionUtils.get;
import static com.asapp.backend.challenge.util.TestConstants.DEFAULT_PASSWORD;
import static com.asapp.backend.challenge.util.TestConstants.DEFAULT_USERNAME;

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

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UserServiceTests {

   @Order(1)
   @Test
   @SneakyThrows
   @DisplayName("User Service :: Creation :: OK")
   void check_user_service_creation_is_OK() {

      final var userResource = UserResource.builder().username(DEFAULT_USERNAME).password(DEFAULT_PASSWORD).build();

      final var userService = new UserService(new UserRepository(get()));
      final var userResponse = userService.create(userResource);

      Assertions.assertThat(userResponse).isNotNull().extracting("id").isNotEqualTo(0);
   }

   @Order(2)
   @Test
   @SneakyThrows
   @DisplayName("User Service :: Find Username :: OK")
   void check_user_service_username_exists() {

      final var userResource = UserResource.builder().username(DEFAULT_USERNAME).password(DEFAULT_PASSWORD).build();

      var userService = new UserService(new UserRepository(get()));
      final var userResponse = userService.create(userResource);

      Assertions.assertThat(userResponse).isNotNull();

      userService = new UserService(new UserRepository(get()));
      final var found = userService.findByUsername(DEFAULT_USERNAME);

      Assertions.assertThat(found).isNotNull().matches(usr -> usr.getId() == found.getId() && usr.getUsername().equals(DEFAULT_USERNAME));
   }

   @SneakyThrows
   @BeforeAll
   static void setup() {
      TestSchemaRunner.run(get());
   }
}
