package io.syebookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(ServiceConfig.class)
public class AppConfiguration implements AutoCloseable {

  public static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

  private ConfigurableApplicationContext context;

  private final ServiceConfig serviceConfig;

  public AppConfiguration(ServiceConfig serviceConfig) {
    this.serviceConfig = serviceConfig;
  }

  public void start() {
    context =
        new SpringApplicationBuilder(AppConfiguration.class)
            .web(WebApplicationType.SERVLET)
            .properties("server.port=" + serviceConfig.port())
            .run();
  }

  public void close() {
    try {
      if (context != null) {
        context.close();
        LOGGER.info("Jetty server stopped.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
