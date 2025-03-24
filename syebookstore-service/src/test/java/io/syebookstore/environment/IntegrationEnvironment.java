package io.syebookstore.environment;


import io.syebookstore.ServiceBootstrap;
import io.syebookstore.ServiceConfig;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SuppressWarnings("rawtypes")
public class IntegrationEnvironment implements AutoCloseable {

  private static final int SMTP_PORT = 1025;
  private static final int API_PORT = 8025;

  private PostgreSQLContainer postgres;
  private GenericContainer<?> mailhog;
  private ServiceBootstrap serviceBootstrap;

  public void start() {
    try {
      postgres = new PostgreSQLContainer("postgres:16-alpine");
      postgres.withExposedPorts(5432);
      postgres.start();

      mailhog =
          new GenericContainer(DockerImageName.parse("mailhog/mailhog"))
              .withExposedPorts(SMTP_PORT, API_PORT);

      mailhog.start();

      serviceBootstrap =
          new ServiceBootstrap(
              new ServiceConfig()
                  .port(8080)
                  .dbUrl(postgres.getJdbcUrl())
                  .dbUsername(postgres.getUsername())
                  .dbPassword(postgres.getPassword())
                  .smtpHost(mailhog.getHost())
                  .smtpPort(mailhog.getMappedPort(SMTP_PORT))
                  .jwtSecret("test12345"));

      serviceBootstrap.start();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public <T> T getBean(Class<T> clazz) {
    return serviceBootstrap.applicationContext().getBean(clazz);
  }

  public static void cleanTables(DataSource dataSource) {
    try (final var connection = dataSource.getConnection()) {
      String truncateQuery =
          "TRUNCATE TABLE accounts RESTART IDENTITY CASCADE; "
              + "TRUNCATE TABLE books RESTART IDENTITY CASCADE; "
              + "TRUNCATE TABLE reviews RESTART IDENTITY CASCADE;";

      try (PreparedStatement statement = connection.prepareStatement(truncateQuery)) {
        statement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() {
    if (postgres != null) {
      postgres.close();
    }
    if (mailhog != null) {
      mailhog.close();
    }
    if (serviceBootstrap != null) {
      serviceBootstrap.close();
    }
  }
}
