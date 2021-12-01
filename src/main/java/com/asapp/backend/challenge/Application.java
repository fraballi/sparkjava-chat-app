package com.asapp.backend.challenge;

import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.controller.AuthController;
import com.asapp.backend.challenge.controller.HealthController;
import com.asapp.backend.challenge.controller.MessagesController;
import com.asapp.backend.challenge.controller.UsersController;
import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.filter.TokenValidatorFilter;
import com.asapp.backend.challenge.runner.SchemaRunner;
import com.asapp.backend.challenge.utils.json.JSONUtil;
import com.asapp.backend.challenge.utils.path.Path;
import com.asapp.backend.challenge.utils.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import spark.Spark;

@Slf4j
public class Application {

   public static void main(String[] args) throws ApiException {

      // Spark Configuration
      Spark.port(8080);

      //Initializers
      SchemaRunner.run();

      // Configure Endpoints
      // Users
      Spark.post(Path.USERS, UsersController.CREATE_USER);
      // Auth
      Spark.post(Path.LOGIN, AuthController.LOGIN);
      // Messages
      Spark.before(Path.MESSAGES, TokenValidatorFilter.VALIDATE_USER);
      Spark.post(Path.MESSAGES, MessagesController.SEND_MESSAGE);
      Spark.get(Path.MESSAGES, MessagesController.GET_MESSAGES);
      // Health
      Spark.post(Path.HEALTH, HealthController.CHECK);

      Spark.exception(Exception.class, (exception, request, response) -> {

         int statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;

         if (exception instanceof ApiException) {
            statusCode = ((ApiException) exception).getStatus();
         }

         try {
            final var error = ErrorResponse.builder().status(statusCode).reason(exception.getMessage()).build();
            response.status(statusCode);
            response.body(JSONUtil.dataToJson(error));

         } catch (Exception e) {
            log.warn(e.getMessage());
         }
      });
   }
}
