package com.asapp.backend.challenge.service;

import java.util.Objects;

import org.eclipse.jetty.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.resources.LoginResource;
import com.asapp.backend.challenge.resources.UserResource;

import lombok.RequiredArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@RequiredArgsConstructor
public final class AuthService {

   private final JwtService jwtService;

   private final UserService userService;

   public LoginResource login(final UserResource userResource) throws ApiException {

      final var entity = userService.findByUsername(userResource.getUsername());
      if (Objects.isNull(entity.getUsername())) {
         throw new ApiException(HttpStatus.NOT_FOUND_404, "User not found");
      }

      if (!BCrypt.checkpw(userResource.getPassword(), entity.getPassword())) {
         throw new ApiException(HttpStatus.BAD_REQUEST_400, "Wrong password");
      }

      final var jwtToken = jwtService.createToken(entity);
      return LoginResource.builder().id(entity.getId()).token(jwtToken).build();
   }

   public void validate(final String token) throws ApiException {
      jwtService.validate(token);
   }
}
