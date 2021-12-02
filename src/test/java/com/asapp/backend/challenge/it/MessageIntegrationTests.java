package com.asapp.backend.challenge.it;

import static com.asapp.backend.challenge.resources.MessageResource.Content;
import static com.asapp.backend.challenge.resources.MessageResource.builder;
import static com.asapp.backend.challenge.util.TestConstants.DEFAULT_PASSWORD;
import static com.asapp.backend.challenge.util.TestConstants.DEFAULT_PORT;
import static com.asapp.backend.challenge.util.TestConstants.LOGIN_RESOURCE;
import static com.asapp.backend.challenge.util.TestConstants.MESSAGES_RESOURCE;
import static com.asapp.backend.challenge.util.TestConstants.USERS_RESOURCE;
import static io.restassured.RestAssured.given;

import java.util.Map;
import java.util.UUID;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;

import org.assertj.core.api.Assertions;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.asapp.backend.challenge.controller.AuthController;
import com.asapp.backend.challenge.controller.MessagesController;
import com.asapp.backend.challenge.controller.UsersController;
import com.asapp.backend.challenge.filter.TokenValidatorFilter;
import com.asapp.backend.challenge.resources.LoginResource;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.runner.SchemaRunner;
import com.asapp.backend.challenge.utils.dto.MessageResponse;
import com.asapp.backend.challenge.utils.dto.UserResponse;
import com.asapp.backend.challenge.utils.path.Path;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Slf4j
class MessageIntegrationTests {

   @Test
   @DisplayName("Messages Controller :: Create :: OK")
   void check_messages_controller_create_is_OK() {

      final var username = UUID.randomUUID().toString();
      final var userResource = UserResource.builder().username(username).password(DEFAULT_PASSWORD).build();
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

      final var loginResource = given()
            .body(userResource)
            .contentType(ContentType.JSON)
            .post(LOGIN_RESOURCE)
            .then()
            .statusCode(HttpStatus.OK_200)
            .contentType(ContentType.JSON)
            .assertThat()
            .extract()
            .body()
            .as(LoginResource.class);

      Assertions.assertThat(loginResource).isNotNull().extracting("token").isNotNull();

      final var content = new Content();
      content.setType(Content.Type.TEXT);
      content.setText("Text Content");

      final var messageResource = builder().sender(1).recipient(1).content(content).build();
      final var messageResponse = given()
            .header(HttpHeader.AUTHORIZATION.name(), "Bearer " + loginResource.getToken())
            .body(messageResource)
            .contentType(ContentType.JSON)
            .post(MESSAGES_RESOURCE)
            .then()
            .statusCode(HttpStatus.OK_200)
            .contentType(ContentType.JSON)
            .assertThat()
            .extract()
            .body()
            .as(MessageResponse.class);

      Assertions.assertThat(messageResponse).isNotNull().extracting("id").isNotNull();
   }

   @Test
   @DisplayName("Messages Controller :: Pagination :: OK")
   void check_messages_controller_pagination_is_OK() {

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

      final var loginResource = given()
            .body(userResource)
            .contentType(ContentType.JSON)
            .post(LOGIN_RESOURCE)
            .then()
            .statusCode(HttpStatus.OK_200)
            .contentType(ContentType.JSON)
            .assertThat()
            .extract()
            .body()
            .as(LoginResource.class);

      Assertions.assertThat(loginResource).isNotNull().extracting("token").isNotNull();

      final var messages = given()
            .queryParam("recipient", 1)
            .queryParam("start", 0)
            .queryParam("limit", 0)
            .header(HttpHeader.AUTHORIZATION.name(), "Bearer " + loginResource.getToken())
            .contentType(ContentType.JSON)
            .get(MESSAGES_RESOURCE)
            .then()
            .statusCode(HttpStatus.OK_200)
            .contentType(ContentType.JSON)
            .assertThat()
            .extract()
            .body()
            .as(new TypeRef<Map<String, ?>>() {

            });

      Assertions.assertThat(messages).containsKey("messages");
   }

   @SneakyThrows
   @BeforeAll
   static void setup() {

      Spark.port(DEFAULT_PORT);

      SchemaRunner.run();

      Spark.post(Path.USERS, UsersController.CREATE_USER);
      Spark.post(Path.LOGIN, AuthController.LOGIN);

      Spark.before(Path.MESSAGES, TokenValidatorFilter.VALIDATE_USER);
      Spark.get(Path.MESSAGES, MessagesController.GET_MESSAGES);
      Spark.post(Path.MESSAGES, MessagesController.SEND_MESSAGE);
   }

   @AfterAll
   static void teardown() {
      Spark.stop();
   }
}
