package com.asapp.backend.challenge.controller;

import com.asapp.backend.challenge.factory.ConnectionFactory;
import com.asapp.backend.challenge.resources.HealthResource;
import com.asapp.backend.challenge.service.HealthService;
import com.asapp.backend.challenge.utils.json.JSONUtil;

import spark.Request;
import spark.Response;
import spark.Route;

public final class HealthController {

   public static final Route CHECK = (Request req, Response rep) -> {

      final var healthService = new HealthService(ConnectionFactory.INSTANCE.get());
      final var databaseStatus = healthService.databaseStatus();

      final HealthResource healthResource = HealthResource.builder().status(databaseStatus).build();
      return JSONUtil.dataToJson(healthResource);

   };

   private HealthController() {
   }
}
