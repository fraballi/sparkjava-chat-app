package com.asapp.backend.challenge.util;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
public final class TestConstants {

   public static final int DEFAULT_SENDER_ID = 1;

   public static final int DEFAULT_RECIPIENT_ID = 1;

   public static final String DEFAULT_USERNAME = "test";

   public static final String DEFAULT_PASSWORD = "test";

   public static final int DEFAULT_PORT = 8181;

   public static final String LOGIN_RESOURCE = "http://localhost:" + DEFAULT_PORT + "/login";

   public static final String USERS_RESOURCE = "http://localhost:" + DEFAULT_PORT + "/users";

   public static final String MESSAGES_RESOURCE = "http://localhost:" + DEFAULT_PORT + "/messages";

   private TestConstants() {
   }
}
