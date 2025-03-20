package io.syebookstore.api.book;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.login;
import static org.junit.jupiter.api.Assertions.fail;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class UploadBooksIT {

  @Test
  void testUploadBookNotLoggedIn(ClientSdk clientSdk) {
    try {
      clientSdk.bookSdk().uploadBook(new UploadBookRequest());
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 403, "Not authenticated");
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testUploadAccountFailedMethodSource")
  void testUploadAccountFailed(FailedArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    try {
      clientSdk.bookSdk().uploadBook(args.request);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, args.errorCode, args.errorMessage);
    }
  }

  private record FailedArgs(
      String test, UploadBookRequest request, int errorCode, String errorMessage) {
    @Override
    public String toString() {
      return test;
    }
  }

  static Stream<FailedArgs> testUploadAccountFailedMethodSource() {
    return Stream.of(
        new FailedArgs("Empty request", new UploadBookRequest(), 400, "Missing or invalid: title"),
        new FailedArgs("Empty request", new UploadBookRequest(), 400, "Missing or invalid: title"));
  }

  @Test
  void testUploadBook(ClientSdk clientSdk, AccountInfo accountInfo) {
    fail("Implement");
  }
}
