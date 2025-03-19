package io.syebookstore.api.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import io.syebookstore.ClientSdk;
import java.util.function.Consumer;

public class AccountAssertions {

  private AccountAssertions() {}

  public static void login(ClientSdk clientSdk, AccountInfo accountInfo) {
    final var token =
        clientSdk
            .accountSdk()
            .login(new LoginRequest().usernameOrEmail(accountInfo.email()).password("test12345"));
    clientSdk.jwtToken(token);
  }

  public static AccountInfo createAccount() {
    return createAccount(null);
  }

  public static AccountInfo createAccount(Consumer<CreateAccountRequest> consumer) {
    try (final var clientSdk = new ClientSdk()) {
      clientSdk.accountSdk();
      final var username = randomAlphanumeric(10);
      final var email =
          randomAlphanumeric(4) + "@" + randomAlphabetic(2, 10) + "." + randomAlphabetic(2, 10);
      final var request =
          new CreateAccountRequest().username(username).email(email).password("test12345");
      if (consumer != null) {
        consumer.accept(request);
      }
      return clientSdk.accountSdk().createAccount(request);
    }
  }

  public static void assertAccount(AccountInfo expected, AccountInfo actual) {
    assertEquals(expected.id(), actual.id(), "actual.id");
    assertEquals(expected.username(), actual.username(), "actual.username");
    assertEquals(expected.email(), actual.email(), "actual.email");
    assertEquals(expected.createdAt(), actual.createdAt(), "actual.createdAt");
    assertNotNull(actual.updatedAt(), "updatedAt");
  }
}
