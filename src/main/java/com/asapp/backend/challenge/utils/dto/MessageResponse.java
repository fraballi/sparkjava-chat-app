package com.asapp.backend.challenge.utils.dto;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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
public class MessageResponse {

   @Builder.Default
   private final String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_INSTANT);

   private long id;
}
