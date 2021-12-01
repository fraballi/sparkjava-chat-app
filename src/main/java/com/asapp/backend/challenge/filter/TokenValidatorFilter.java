package com.asapp.backend.challenge.filter;

import static com.asapp.backend.challenge.utils.jwt.JwtUtils.BEARER;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.factory.ConnectionFactory;
import com.asapp.backend.challenge.repository.UserRepository;
import com.asapp.backend.challenge.service.AuthService;
import com.asapp.backend.challenge.service.JwtService;
import com.asapp.backend.challenge.service.UserService;
import com.asapp.backend.challenge.utils.jwt.JwtUtils;

import spark.Filter;
import spark.Request;
import spark.Response;

public final class TokenValidatorFilter {

   public static final Filter VALIDATE_USER = (Request req, Response resp) -> {
      final var authorizationHeader = req.headers(HttpHeader.AUTHORIZATION.asString());

      if (!JwtUtils.isBearer(authorizationHeader)) {
         throw new ApiException(HttpStatus.UNAUTHORIZED_401, "Wrong authorization headers");
      }

      final var beginIndex = authorizationHeader.indexOf(BEARER);
      final var token = JwtUtils.extractToken(authorizationHeader.substring(beginIndex));

      final var userRepository = new UserRepository(ConnectionFactory.INSTANCE.get());
      final var authService = new AuthService(new JwtService(), new UserService(userRepository));

      authService.validate(token);
   };

   private TokenValidatorFilter() {
   }
}
