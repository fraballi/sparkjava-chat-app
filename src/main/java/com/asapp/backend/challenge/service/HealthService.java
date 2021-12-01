package com.asapp.backend.challenge.service;

import static com.asapp.backend.challenge.resources.HealthResource.Status.ERROR;
import static com.asapp.backend.challenge.resources.HealthResource.Status.OK;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import com.asapp.backend.challenge.resources.HealthResource;

import lombok.RequiredArgsConstructor;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
@RequiredArgsConstructor
public final class HealthService {

   private final Connection connection;

   public HealthResource.Status databaseStatus() throws SQLException {
      return Objects.nonNull(connection) && connection.isValid(1000) ? OK : ERROR;
   }
}
