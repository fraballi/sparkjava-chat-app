package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.factory.ConnectionFactory;
import com.asapp.backend.challenge.repository.UserRepository;
import com.asapp.backend.challenge.resources.UserResource;
import com.asapp.backend.challenge.service.AuthService;
import com.asapp.backend.challenge.service.JwtService;
import com.asapp.backend.challenge.service.UserService;
import com.asapp.backend.challenge.utils.json.JSONUtil;

import spark.Request;
import spark.Response;
import spark.Route;

public final class AuthController {

    public static final Route LOGIN = (Request req, Response resp) -> {

        final var userRepository = new UserRepository(ConnectionFactory.INSTANCE.get());
        final var authService = new AuthService(new JwtService(), new UserService(userRepository));

        final var userResource = JSONUtil.dataToModel(req.body(), UserResource.class);
        final var loginResource = authService.login(userResource);

        return JSONUtil.dataToJson(loginResource);
    };

    private AuthController() {
    }
}
