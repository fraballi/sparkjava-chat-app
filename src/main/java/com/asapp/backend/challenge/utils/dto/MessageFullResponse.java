package com.asapp.backend.challenge.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageFullResponse {

   private int id;

   private int sender;

   private int recipient;

   private String timestamp;

   private Content content;

   @Data
   @Builder
   @AllArgsConstructor
   @NoArgsConstructor
   public static class Content {

      private String type;

      private String text;

   }
}
