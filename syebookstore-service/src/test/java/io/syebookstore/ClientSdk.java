package io.syebookstore;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.syebookstore.api.account.AccountSdk;
import io.syebookstore.api.ServiceException;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSdk implements AutoCloseable {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientSdk.class);

  private String jwtToken;

  private final JsonMapper objectMapper = JsonMappers.jsonMapper();
  private final HttpClient client;

  public ClientSdk() {
    client = HttpClient.newHttpClient();
  }

  private <T> T sendRequest(String name, Object data, Class<T> responseType) {
    try {
      final var requestBuilder =
          HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/v1/" + name))
              .header("Content-Type", "application/json")
              .POST(BodyPublishers.ofString(objectMapper.writeValueAsString(data)));

      if (jwtToken != null) {
        requestBuilder.header("Authorization", jwtToken);
      }
      final var request = requestBuilder.build();

      final var response = client.send(request, BodyHandlers.ofString());

      final var statusCode = response.statusCode();
      if (statusCode == 200) {
        if (responseType.isAssignableFrom(Void.class)) {
          return null;
        }
        if (responseType.isAssignableFrom(String.class)) {
          //noinspection unchecked
          return (T) response.body();
        }
        return objectMapper.readValue(response.body(), responseType);
      }
      if (statusCode == 204) {
        return null;
      }

      throw new ServiceException(statusCode, response.body());
    } catch (ServiceException ex) {
     throw ex;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public <T> T api(Class<T> api) {
    if (!api.isInterface()) {
      throw new IllegalArgumentException("Must be interface: " + api);
    }
    //noinspection unchecked
    return (T)
        Proxy.newProxyInstance(
            api.getClassLoader(),
            new Class[] {api},
            (proxy, method, args) -> {
              Object check = toStringOrEqualsOrHashCode(method.getName(), api, args);
              if (check != null) {
                return check;
              }
              final var data = args != null ? args[0] : null;
              final var name = method.getName();
              //noinspection unchecked
              final var returnType = (Class<T>) method.getReturnType();

              LOGGER.debug("Send: {}", data);
              return sendRequest(name, data, returnType);
            });
  }

  public AccountSdk accountSdk() {
    return api(AccountSdk.class);
  }

  public String jwtToken() {
    return jwtToken;
  }

  public ClientSdk jwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
    return this;
  }

  private Object toStringOrEqualsOrHashCode(String method, Class<?> api, Object... args) {
    return switch (method) {
      case "toString" -> api.toString();
      case "equals" -> api.equals(args[0]);
      case "hashCode" -> api.hashCode();
      default -> null;
    };
  }

  @Override
  public void close() {
    client.close();
  }
}
