package com.asapp.backend.challenge.utils.json;

import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.jetty.http.HttpStatus;

import com.asapp.backend.challenge.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JSONUtil {

   private static final ObjectMapper MAPPER = new ObjectMapper();

   static {
      MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
   }

   private JSONUtil() {
   }

   public static String dataToJson(Object data) throws ApiException {
      final var sw = new StringWriter();
      try {
         MAPPER.writeValue(sw, data);
      } catch (IOException e) {
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, e.getMessage());
      }
      return sw.toString();
   }

   public static <T> T dataToModel(final String data, Class<T> clazz) throws ApiException {
      try {
         return MAPPER.readValue(data, clazz);
      } catch (JsonProcessingException e) {
         throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR_500, e.getMessage());
      }
   }
}
