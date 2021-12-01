package com.asapp.backend.challenge.service;

import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.utils.config.ConfigParser;
import com.asapp.backend.challenge.utils.dto.JwtProperties;
import com.asapp.backend.challenge.utils.jwt.JwtUtils;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
public final class JwtService {

   public void validate(final String token) throws ApiException {
      final var jwtProperties = ConfigParser.getJwtProperties();
      final var properties = JwtProperties.builder().issuer(jwtProperties.getIssuer()).clientSecret(jwtProperties.getClientSecret()).build();

      if (!JwtUtils.validate(token, properties)) {
         throw new ApiException(HttpStatus.UNAUTHORIZED_401, "Invalid Bearer token");
      }
   }

   public String createToken(final UserResource userResource) {
      final var jwtProperties = ConfigParser.getJwtProperties();
      final var properties = JwtProperties
            .builder()
            .issuer(jwtProperties.getIssuer())
            .clientSecret(jwtProperties.getClientSecret())
            .tokenExpirationSeconds(jwtProperties.getTokenExpirationSeconds())
            .build();

      return JwtUtils.createToken(userResource, properties);
   }
}
