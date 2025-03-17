package io.syebookstore;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

  @Bean
  public DataSource dataSource(ServiceConfig serviceConfig) {
    final var hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(serviceConfig.dbUrl());
    hikariConfig.setUsername(serviceConfig.dbUsername());
    hikariConfig.setPassword(serviceConfig.dbPassword());
    hikariConfig.setConnectionInitSql("CREATE SCHEMA IF NOT EXISTS syebookstore");
    hikariConfig.setSchema("syebookstore");
    return new HikariDataSource(hikariConfig);
  }
}
