package com.asapp.backend.challenge.resources;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class MessageResource {

   @Builder.Default
   private final String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_INSTANT);

   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private int id;

   @JsonAlias({ "userId" })
   private int sender;

   @JsonAlias({ "recipientId" })
   private int recipient;

   @NotNull
   private Content content;

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   @Valid
   public static class Content {

      private Type type = Type.TEXT;

      @NotNull
      private String text;

      public enum Type {
         TEXT,
         IMAGE,
         VIDEO;
      }
   }
}
