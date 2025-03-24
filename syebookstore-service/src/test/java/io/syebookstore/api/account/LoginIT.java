package io.syebookstore.api.account;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.assertAccount;
import static io.syebookstore.api.account.AccountAssertions.createAccount;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import io.syebookstore.ClientSdk;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import java.util.StringJoiner;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class LoginIT {

  private static AccountInfo existingAccountInfo;

  @BeforeAll
  static void beforeAll() {
    existingAccountInfo = createAccount();
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testLoginFailedMethodSource")
  void testLoginFailed(FailedArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
    try {
      clientSdk.accountSdk().login(args.request);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, args.errorCode, args.errorMessage);
    }
  }

  private record FailedArgs(String test, LoginRequest request, int errorCode, String errorMessage) {
    @Override
    public String toString() {
      return new StringJoiner(", ", FailedArgs.class.getSimpleName() + "[", "]")
          .add("test='" + test + "'")
          .add("request=" + request)
          .add("errorCode=" + errorCode)
          .add("errorMessage='" + errorMessage + "'")
          .toString();
    }
  }

  static Stream<FailedArgs> testLoginFailedMethodSource() {
    return Stream.of(
        new FailedArgs(
            "Empty request", new LoginRequest(), 400, "Missing or invalid: username or email"),
        new FailedArgs(
            "usernameOrEmail is empty string",
            new LoginRequest().usernameOrEmail("").password(randomAlphanumeric(10)),
            400,
            "Missing or invalid: username or email"),
        new FailedArgs(
            "Password is null",
            new LoginRequest().usernameOrEmail(randomAlphanumeric(10)),
            400,
            "Missing or invalid: password"),
        new FailedArgs(
            "Password is empty string",
            new LoginRequest().usernameOrEmail(randomAlphanumeric(10)).password(null),
            400,
            "Missing or invalid: password"),
        new FailedArgs(
            "Login to non-existing account",
            new LoginRequest()
                .usernameOrEmail(randomAlphanumeric(10))
                .password(randomAlphanumeric(10)),
            404,
            "Cannot login: account not found"),
        new FailedArgs(
            "Invalid password",
            new LoginRequest()
                .usernameOrEmail(existingAccountInfo.username())
                .password(randomAlphanumeric(8, 65)),
            400,
            "Login failed"));
  }

  @Test
  void testLogin(ClientSdk clientSdk, AccountInfo existingAccountInfo) {
    final var token =
        clientSdk
            .accountSdk()
            .login(
                new LoginRequest()
                    .usernameOrEmail(existingAccountInfo.email())
                    .password("test12345"));

    assertNotNull(token);

    clientSdk.jwtToken(token);
    final var accountInfo = clientSdk.accountSdk().getAccount(null);
    assertAccount(existingAccountInfo, accountInfo);
  }

  @Test
  void testWrongToken(ClientSdk clientSdk, AccountInfo accountInfo) {
    try {
      clientSdk.jwtToken("test");
      clientSdk.accountSdk().getAccount(null);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 403, "Not authenticated");
    }
  }
}
