package io.syebookstore;

import java.util.StringJoiner;

public class ServiceConfig {

  private int port;
  private String dbUrl;
  private String dbUsername;
  private String dbPassword;
  private String jwtSecret;

  public static ServiceConfig fromSystemProperties() {
    final var port = getProperty("port");
    final var dbUrl = getProperty("dbUrl");
    final var dbUsername = getProperty("dbUsername");
    final var dbPassword = getProperty("dbPassword");
    final var jwtSecret = getProperty("jwtSecret");

    return new ServiceConfig()
        .port(Integer.parseInt(port))
        .dbUrl(dbUrl)
        .dbUsername(dbUsername)
        .dbPassword(dbPassword)
        .jwtSecret(jwtSecret);
  }

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

  private static String getProperty(String property) {
    return getProperty(property, false);
  }

  private static String getProperty(String property, boolean isOptional) {
    final var value = System.getProperty("syebookstore." + property);
    if (!isOptional && value == null) {
      throw new RuntimeException("Wrong config: missing " + property);
    }
    return value;
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
