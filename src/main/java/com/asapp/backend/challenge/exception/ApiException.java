package com.asapp.backend.challenge.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApiException extends Exception {

   private final int status;

   public ApiException(final int status, final String message) {
      super(message);
      this.status = status;
   }
}
