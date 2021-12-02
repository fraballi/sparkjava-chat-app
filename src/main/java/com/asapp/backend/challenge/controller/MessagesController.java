package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.factory.ConnectionFactory;
import com.asapp.backend.challenge.repository.MessageRepository;
import com.asapp.backend.challenge.resources.MessageResource;
import com.asapp.backend.challenge.service.MessageService;
import com.asapp.backend.challenge.utils.json.JSONUtil;

import spark.Request;
import spark.Response;
import spark.Route;

public final class MessagesController {

    public static final Route SEND_MESSAGE = (Request req, Response rep) -> {

        final var messageRepository = new MessageRepository(ConnectionFactory.INSTANCE.get());
        final var messageService = new MessageService(messageRepository);

        final var messageResource = JSONUtil.dataToModel(req.body(), MessageResource.class);
        final var messageResponse = messageService.create(messageResource);

        rep.type("application/json");
        return JSONUtil.dataToJson(messageResponse);
    };

    public static final Route GET_MESSAGES = (Request req, Response rep) -> {

        final var messageRepository = new MessageRepository(ConnectionFactory.INSTANCE.get());
        final var messageService = new MessageService(messageRepository);

        final var queryString = req.queryMap();
        final var recipient = queryString.get("recipient").integerValue();
        final var start = queryString.get("start").integerValue();
        final var limit = queryString.get("limit").integerValue();

        final var messages = messageService.getAll(recipient, start, limit);

        rep.type("application/json");
        return JSONUtil.dataToJson(messages);
    };

    private MessagesController() {
    }
}
