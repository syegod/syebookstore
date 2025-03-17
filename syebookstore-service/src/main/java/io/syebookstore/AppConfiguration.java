package io.syebookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
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
            .initializers(
                applicationContext -> {
                  final var genericApplicationContext =
                      (GenericApplicationContext) applicationContext;
                  genericApplicationContext.registerBean(
                      ServiceConfig.class, () -> serviceConfig, bd -> {});
                })
            .web(WebApplicationType.SERVLET)
            .bannerMode(Mode.OFF)
            .properties("server.port=" + serviceConfig.port())
            .run();
  }

  public void close() {
    if (context != null) {
      context.close();
      LOGGER.info("Jetty server stopped");
    }
  }
}
