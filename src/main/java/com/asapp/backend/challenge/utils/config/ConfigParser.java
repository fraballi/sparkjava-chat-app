package com.asapp.backend.challenge.utils.config;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.yaml.snakeyaml.Yaml;

import com.asapp.backend.challenge.utils.dto.JwtProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
public final class ConfigParser {

   private ConfigParser() {
   }

   public static String getDataSourceUrl() {
      final var config = loadConfigFile();
      return config.getDatasource().getUrl();
   }

   public static JwtProperties getJwtProperties() {

      final var config = loadConfigFile();

      return JwtProperties
            .builder()
            .issuer(config.getJwt().getIssuer())
            .clientSecret(config.getJwt().getClientSecret())
            .tokenExpirationSeconds(config.getJwt().getExpirationSeconds())
            .build();
   }

   private static App loadConfigFile() {
      final var yaml = new Yaml();

      final var classLoader = Thread.currentThread().getContextClassLoader();
      final var resource = classLoader.getResourceAsStream("application.yml");
      return yaml.loadAs(resource, App.class);
   }

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   @Valid
   public static class App {

      @NotEmpty
      private Jwt jwt;

      @NotEmpty
      private DataSource datasource;

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      @Valid
      public static class DataSource {

         @NotNull
         private String url;
      }

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      @Valid
      public static class Jwt {

         @NotNull
         private String issuer;

         @NotNull
         private String clientSecret;

         private int expirationSeconds;
      }
   }
}
