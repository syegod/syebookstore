package io.syebookstore.environment;

import io.syebookstore.AppConfiguration;
import io.syebookstore.ServiceConfig;
import org.testcontainers.containers.PostgreSQLContainer;

public class IntegrationEnvironment implements AutoCloseable {

  private PostgreSQLContainer postgres;
  private AppConfiguration appConfiguration;

  public void start() {
    try {
      postgres = new PostgreSQLContainer("postgres:16-alpine");
      postgres.withExposedPorts(5432);
      postgres.start();

      appConfiguration =
          new AppConfiguration(
              new ServiceConfig()
                  .port(8080)
                  .dbUrl(postgres.getJdbcUrl())
                  .dbUsername(postgres.getUsername())
                  .dbPassword(postgres.getPassword()));

      appConfiguration.start();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void close() {
    if (postgres != null) {
      postgres.close();
    }
    if (appConfiguration != null) {
      appConfiguration.close();
    }
  }
}
