package io.syebookstore;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MyDataSourceConfiguration {

  @Bean
  public HikariDataSource dataSource(ServiceConfig serviceConfig) {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }
}
