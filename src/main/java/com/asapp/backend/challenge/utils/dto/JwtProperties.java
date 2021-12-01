package com.asapp.backend.challenge.utils.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
@Valid
public class JwtProperties {

   @NotNull
   private String issuer;

   @NotNull
   private String clientSecret;

   private int tokenExpirationSeconds;
}
