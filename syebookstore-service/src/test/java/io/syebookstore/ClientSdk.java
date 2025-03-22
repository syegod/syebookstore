package io.syebookstore;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.syebookstore.api.ServiceException;
import io.syebookstore.api.account.AccountSdk;
import io.syebookstore.api.book.BookSdk;
import io.syebookstore.api.review.ReviewSdk;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
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

  private <T> T sendRequest(String rootUrl, String name, Object data, Class<T> responseType) {
    try {
      final var requestBuilder =
          HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:8080/v1" + rootUrl + "/" + name))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(data)));

      if (jwtToken != null) {
        requestBuilder.header("Authorization", "Bearer " + jwtToken);
      }
      final var request = requestBuilder.build();

      final var response = client.send(request, BodyHandlers.ofByteArray());

      final var statusCode = response.statusCode();
      byte[] responseBody = response.body();

      if (statusCode == 200) {
        if (responseType.isAssignableFrom(byte[].class)) {
          //noinspection unchecked
          return (T) responseBody;
        }

        String responseBodyString = new String(responseBody, StandardCharsets.UTF_8);

        if (responseType.isAssignableFrom(String.class)) {
          //noinspection unchecked
          return (T) responseBodyString;
        }
        return objectMapper.readValue(responseBodyString, responseType);
      }

      String errorResponseBody = new String(responseBody, StandardCharsets.UTF_8);

      throw new ServiceException(statusCode, errorResponseBody);
    } catch (ServiceException ex) {
      throw ex;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T api(Class<T> api, String rootUrl) {
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
              return sendRequest(rootUrl, name, data, returnType);
            });
  }

  public AccountSdk accountSdk() {
    return api(AccountSdk.class, "/account");
  }

  public BookSdk bookSdk() {
    return api(BookSdk.class, "/book");
  }

  public ReviewSdk reviewSdk() {
    return api(ReviewSdk.class, "/review");
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
