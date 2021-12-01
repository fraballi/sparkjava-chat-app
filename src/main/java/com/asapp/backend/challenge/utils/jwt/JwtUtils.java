package com.asapp.backend.challenge.utils.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.exception.ApiException;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.utils.dto.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import spark.utils.StringUtils;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
public final class JwtUtils {

   public static final String BEARER = "Bearer";

   private static final String USERNAME = "username";

   private JwtUtils() {
   }

   public static boolean isBearer(final String header) {
      return StringUtils.isNotEmpty(header) && header.startsWith(BEARER);
   }

   public static String extractToken(final String header) {
      return StringUtils.isNotEmpty(header) ? header.substring(BEARER.length()).trim() : "";
   }

   public static String createToken(final UserResource userResource, final JwtProperties properties) {
      final var now = Instant.now();
      final var tokenExpirationSeconds = properties.getTokenExpirationSeconds();
      final var expireAtInstant = now.plusSeconds(tokenExpirationSeconds);

      final var issuedAt = Date.from(now);
      final var expireAt = Date.from(expireAtInstant);

      final var clientSecret = properties.getClientSecret();
      final var algorithm = Algorithm.HMAC512(clientSecret);

      final var issuer = properties.getIssuer();
      return JWT.create().withIssuer(issuer).withIssuedAt(issuedAt).withExpiresAt(expireAt).withClaim(USERNAME, userResource.getUsername())
            .sign(algorithm);
   }

   public static boolean validate(final String token, final JwtProperties properties) throws ApiException {
      return Objects.nonNull(parseToken(token, properties));
   }

   public static UserResource parseToken(final String token, final JwtProperties properties) throws ApiException {
      final var clientSecret = properties.getClientSecret();
      final var algorithm = Algorithm.HMAC512(clientSecret);

      final var issuer = properties.getIssuer();
      final var verification = JWT.require(algorithm).withIssuer(issuer);

      DecodedJWT verifiedToken;
      try {
         verifiedToken = verification.build().verify(token);
      } catch (JWTVerificationException e) {
         throw new ApiException(HttpStatus.BAD_REQUEST_400, e.getMessage());
      }

      final var username = verifiedToken.getClaim(USERNAME).asString();
      return UserResource.builder().username(username).build();
   }
}
