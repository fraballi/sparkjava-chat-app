package com.asapp.backend.challenge.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
public class LoginResource {

   @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private int id;

   @NotNull
   private String token;
}
