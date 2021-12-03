package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.factory.ConnectionFactory;
import com.asapp.backend.challenge.repository.UserRepository;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.service.UserService;
import com.asapp.backend.challenge.utils.json.JSONUtil;

import spark.Request;
import spark.Response;
import spark.Route;

public final class UsersController {

   public static final Route CREATE_USER = (Request req, Response resp) -> {

      final UserRepository userRepository = new UserRepository(ConnectionFactory.INSTANCE.get());
      final UserService service = new UserService(userRepository);

      final var userResource = JSONUtil.dataToModel(req.body(), UserResource.class);
      final var userId = service.create(userResource);

      resp.type("application/json");
      return JSONUtil.dataToJson(userId);
   };

   private UsersController() {
   }
}
