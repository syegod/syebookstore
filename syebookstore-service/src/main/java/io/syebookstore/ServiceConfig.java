package io.syebookstore;

import java.util.StringJoiner;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "syebookstore")
public class ServiceConfig {

  private int port = 8080;
  private String dbUrl;
  private String dbUsername;
  private String dbPassword;

  public int port() {
    return port;
  }

  public ServiceConfig port(int port) {
    this.port = port;
    return this;
  }

  public String dbUrl() {
    return dbUrl;
  }

  public ServiceConfig dbUrl(String dbUrl) {
    this.dbUrl = dbUrl;
    return this;
  }

  public String dbUsername() {
    return dbUsername;
  }

  public ServiceConfig dbUsername(String dbUser) {
    this.dbUsername = dbUser;
    return this;
  }

  public String dbPassword() {
    return dbPassword;
  }

  public ServiceConfig dbPassword(String dbPassword) {
    this.dbPassword = dbPassword;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ServiceConfig.class.getSimpleName() + "[", "]")
        .add("port=" + port)
        .add("dbUrl='" + dbUrl + "'")
        .add("dbUsername='" + dbUsername + "'")
        .add("dbPassword='" + dbPassword + "'")
        .toString();
  }
}
