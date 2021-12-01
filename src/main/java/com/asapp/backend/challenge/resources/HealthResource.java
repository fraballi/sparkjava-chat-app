package com.asapp.backend.challenge.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthResource {

   @JsonProperty("health")
   private Status status;

   public enum Status {
      OK,
      ERROR
   }
}
