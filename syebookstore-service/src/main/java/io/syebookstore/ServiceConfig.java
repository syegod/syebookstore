package io.syebookstore;

import java.util.StringJoiner;

public class ServiceConfig {

  private int port;
  private String host;
  private String dbUrl;
  private String dbUsername;
  private String dbPassword;
  private String jwtSecret;
  private String smtpHost;
  private int smtpPort;

  public static ServiceConfig fromSystemProperties() {
    final var port = getProperty("port");
    final var host = getProperty("host");
    final var dbUrl = getProperty("dbUrl");
    final var dbUsername = getProperty("dbUsername");
    final var dbPassword = getProperty("dbPassword");
    final var jwtSecret = getProperty("jwtSecret");
    final var smtpHost = getProperty("smtpHost");
    final var smtpPort = getProperty("smtpPort");

    return new ServiceConfig()
        .port(Integer.parseInt(port))
        .host(host)
        .dbUrl(dbUrl)
        .dbUsername(dbUsername)
        .dbPassword(dbPassword)
        .jwtSecret(jwtSecret)
        .smtpHost(smtpHost)
        .smtpPort(Integer.parseInt(smtpPort));
  }

  public int port() {
    return port;
  }

  public ServiceConfig port(int port) {
    this.port = port;
    return this;
  }

  public String host() {
    return host;
  }

  public ServiceConfig host(String host) {
    this.host = host;
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

  public String smtpHost() {
    return smtpHost;
  }

  public ServiceConfig smtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
    return this;
  }

  public int smtpPort() {
    return smtpPort;
  }

  public ServiceConfig smtpPort(int smtpPort) {
    this.smtpPort = smtpPort;
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
        .add("host='" + host + "'")
        .add("dbUrl='" + dbUrl + "'")
        .add("dbUsername='" + dbUsername + "'")
        .add("dbPassword='" + dbPassword + "'")
        .add("jwtSecret='" + jwtSecret + "'")
        .add("smtpHost='" + smtpHost + "'")
        .add("smtpPort=" + smtpPort)
        .toString();
  }
}
