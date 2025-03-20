package io.syebookstore;

import java.util.StringJoiner;

public class ServiceConfig {

  private int port = 8080;
  private String dbUrl;
  private String dbUsername;
  private String dbPassword;
  private String jwtSecret;

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

  public String jwtSecret() {
    return jwtSecret;
  }

  public ServiceConfig jwtSecret(String jwtSecret) {
    this.jwtSecret = jwtSecret;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ServiceConfig.class.getSimpleName() + "[", "]")
        .add("port=" + port)
        .add("dbUrl='" + dbUrl + "'")
        .add("dbUsername='" + dbUsername + "'")
        .add("dbPassword='" + dbPassword + "'")
        .add("jwtSecret='" + jwtSecret + "'")
        .toString();
  }
}
