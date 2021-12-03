package com.asapp.backend.challenge.it;

import static com.asapp.backend.challenge.util.TestConstants.DEFAULT_PORT;
import static com.asapp.backend.challenge.util.TestConstants.USERS_RESOURCE;
import static io.restassured.RestAssured.given;

import java.util.UUID;

import io.restassured.http.ContentType;

import org.assertj.core.api.Assertions;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.asapp.backend.challenge.controller.UsersController;
import com.asapp.backend.challenge.runner.SchemaRunner;
import com.asapp.backend.challenge.utils.dto.UserResponse;
import com.asapp.backend.challenge.utils.path.Path;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
class UserIntegrationTests {

   @Test
   @DisplayName("User Controller :: Creation :: OK")
   void check_user_controller_creation_is_OK() {

      final var username = UUID.randomUUID().toString();
      final var userResource = "{ \"username\": \"" + username + "\", \"password\": \"test\"}";
      final var userResponse = given()
            .body(userResource)
            .contentType(ContentType.JSON)
            .post(USERS_RESOURCE)
            .then()
            .statusCode(HttpStatus.OK_200)
            .contentType(ContentType.JSON)
            .assertThat()
            .extract()
            .body()
            .as(UserResponse.class);

      Assertions.assertThat(userResponse).isNotNull().extracting("id").isNotEqualTo(0);
   }

   @SneakyThrows
   @BeforeAll
   static void setup() {

      Spark.port(DEFAULT_PORT);

      SchemaRunner.run();

      Spark.post(Path.USERS, UsersController.CREATE_USER);
   }

   @AfterAll
   static void teardown() {
      Spark.stop();
   }
}
