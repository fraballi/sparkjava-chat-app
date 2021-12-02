package com.asapp.backend.challenge.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.yaml.snakeyaml.Yaml;

import com.asapp.backend.challenge.utils.config.ConfigParser;

import lombok.SneakyThrows;

/**
 * @author Felix Aballi <felixaballi@gmail.com>
 */
public final class TestConnectionUtils {

   private final String file;

   private TestConnectionUtils(final String file) {
      this.file = file;
   }

   public String getDataSourceUrl() {
      return loadConfigFile().getDatasource().getUrl();
   }

   private ConfigParser.App loadConfigFile() {
      final var yaml = new Yaml();

      final var classLoader = Thread.currentThread().getContextClassLoader();
      final var resource = classLoader.getResourceAsStream(file);
      return yaml.loadAs(resource, ConfigParser.App.class);
   }

   public static TestConnectionUtils from(final String file) {
      return new TestConnectionUtils(file);
   }

   @SneakyThrows
   public static Connection get() {

      final var dataSourceUrl = TestConnectionUtils.from("application-test.yml").getDataSourceUrl();
      final var connection = DriverManager.getConnection(dataSourceUrl);
      connection.setAutoCommit(false);

      return connection;
   }
}
