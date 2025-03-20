package io.syebookstore.api.account;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.createAccount;
import static io.syebookstore.api.account.AccountAssertions.login;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import io.syebookstore.ClientSdk;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class UpdateAccountIT {

  private static AccountInfo existingAccountInfo;

  @BeforeAll
  static void beforeAll() {
    existingAccountInfo = createAccount();
  }

  @Test
  void testUpdateAccountNotLoggedIn(ClientSdk clientSdk, AccountInfo existingAccountInfo) {
    try {
      clientSdk.accountSdk().updateAccount(new UpdateAccountRequest());
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 403, "Not authenticated");
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testUpdateAccountFailedMethodSource")
  void testUpdateAccountFailed(FailedArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    try {
      clientSdk.accountSdk().updateAccount(args.request);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, args.errorCode, args.errorMessage);
    }
  }

  private record FailedArgs(
      String test, UpdateAccountRequest request, int errorCode, String errorMessage) {
    @Override
    public String toString() {
      return test;
    }
  }

  private static Stream<?> testUpdateAccountFailedMethodSource() {
    return Stream.of(
        new FailedArgs(
            "Username too long",
            new UpdateAccountRequest().username(randomAlphanumeric(80)),
            400,
            "Invalid: username"),
        new FailedArgs(
            "Username too short",
            new UpdateAccountRequest().username(randomAlphanumeric(7)),
            400,
            "Invalid: username"),
        new FailedArgs(
            "Wrong email type",
            new UpdateAccountRequest().email(randomAlphanumeric(8, 65)),
            400,
            "Invalid: email"),
        new FailedArgs(
            "Email too long",
            new UpdateAccountRequest().email(randomAlphanumeric(80)),
            400,
            "Invalid: email"),
        new FailedArgs(
            "Email too short",
            new UpdateAccountRequest().email(randomAlphanumeric(7)),
            400,
            "Invalid: email"),
        new FailedArgs(
            "Password too long",
            new UpdateAccountRequest().password(randomAlphanumeric(80)),
            400,
            "Invalid: password"),
        new FailedArgs(
            "Password too short",
            new UpdateAccountRequest().password(randomAlphanumeric(7)),
            400,
            "Invalid: password"),
        new FailedArgs(
            "Description too short",
            new UpdateAccountRequest().description(randomAlphanumeric(7)),
            400,
            "Invalid: description"),
        new FailedArgs(
            "Description too long",
            new UpdateAccountRequest().description(randomAlphanumeric(501)),
            400,
            "Invalid: description"),
        new FailedArgs(
            "Updating username to already existing",
            new UpdateAccountRequest().username(existingAccountInfo.username()),
            400,
            "Cannot update account: already exists"),
        new FailedArgs(
            "Updating email to already existing",
            new UpdateAccountRequest().email(existingAccountInfo.email()),
            400,
            "Cannot update account: already exists"));
  }

  @Test
  void testUpdateAccount(ClientSdk clientSdk, AccountInfo existingAccountInfo) {
    login(clientSdk, existingAccountInfo);
    final var username = "test54321";
    final var email = "test54321@gmail.com";
    final var accountInfo =
        clientSdk
            .accountSdk()
            .updateAccount(
                new UpdateAccountRequest().username(username).email(email).password("test54321"));

    assertEquals(existingAccountInfo.id(), accountInfo.id());
    assertEquals(username, accountInfo.username());
    assertEquals(email, accountInfo.email());
    assertEquals(existingAccountInfo.description(), accountInfo.description());
    assertEquals(AccountStatus.NON_CONFIRMED, accountInfo.status());
    assertEquals(existingAccountInfo.createdAt(), accountInfo.createdAt());
    assertNotNull(existingAccountInfo.updatedAt());
  }
}
