package io.syebookstore.environment;

import io.syebookstore.ServiceBootstrap;
import io.syebookstore.ServiceConfig;
import org.testcontainers.containers.PostgreSQLContainer;

public class IntegrationEnvironment implements AutoCloseable {

  private PostgreSQLContainer postgres;
  private ServiceBootstrap serviceBootstrap;

  public void start() {
    try {
      postgres = new PostgreSQLContainer("postgres:16-alpine");
      postgres.withExposedPorts(5432);
      postgres.start();

      serviceBootstrap =
          new ServiceBootstrap(
              new ServiceConfig()
                  .port(8080)
                  .dbUrl(postgres.getJdbcUrl())
                  .dbUsername(postgres.getUsername())
                  .dbPassword(postgres.getPassword())
                  .jwtSecret("test12345"));

      serviceBootstrap.start();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void close() {
    if (postgres != null) {
      postgres.close();
    }
    if (serviceBootstrap != null) {
      serviceBootstrap.close();
    }
  }
}
