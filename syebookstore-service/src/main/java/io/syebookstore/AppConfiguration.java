package io.syebookstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.mail.Session;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

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

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
    objectMapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.ANY);
    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Bean
  public Session mailSession(ServiceConfig serviceConfig) {
    final var properties = new Properties();
    properties.put("mail.smtp.host", serviceConfig.smtpHost());
    properties.put("mail.smtp.port", serviceConfig.smtpPort());
    return Session.getInstance(properties);
  }
}
