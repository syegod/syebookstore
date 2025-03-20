package io.syebookstore.api.book;

import static io.syebookstore.AssertionUtils.ra;
import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookAssertions.assertBookRequest;
import static org.junit.jupiter.api.Assertions.fail;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class UploadBookIT {

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
        new FailedArgs(
            "Title too short",
            new UploadBookRequest().title(ra(7)),
            400,
            "Missing or invalid: title"),
        new FailedArgs(
            "Title too long",
            new UploadBookRequest().title(ra(65)),
            400,
            "Missing or invalid: title"),
        new FailedArgs(
            "No isbn", new UploadBookRequest().title(ra(10)), 400, "Missing or invalid: isbn"),
        new FailedArgs(
            "Isbn is not 13 char length",
            new UploadBookRequest().title(ra(10)).isbn(ra(10)),
            400,
            "Missing or invalid: isbn"),
        new FailedArgs(
            "No description",
            new UploadBookRequest().title(ra(10)).isbn(ra(13)),
            400,
            "Missing or invalid: description"),
        new FailedArgs(
            "Description too short",
            new UploadBookRequest().title(ra(10)).isbn(ra(13)).description(ra(7)),
            400,
            "Missing or invalid: description"),
        new FailedArgs(
            "Description too long",
            new UploadBookRequest().title(ra(10)).isbn(ra(13)).description(ra(501)),
            400,
            "Missing or invalid: description"),
        new FailedArgs(
            "No publication date",
            new UploadBookRequest().title(ra(10)).isbn(ra(13)).description(ra(8)),
            400,
            "Missing or invalid: publication date"),
        new FailedArgs(
            "Publication date is in future",
            new UploadBookRequest()
                .title(ra(10))
                .isbn(ra(13))
                .description(ra(8))
                .publicationDate(2030),
            400,
            "Missing or invalid: publication date"),
        new FailedArgs(
            "No content",
            new UploadBookRequest()
                .title(ra(10))
                .isbn(ra(13))
                .description(ra(8))
                .publicationDate(2020),
            400,
            "Missing or invalid: content"),
        new FailedArgs(
            "No authors",
            new UploadBookRequest()
                .title(ra(10))
                .isbn(ra(13))
                .content(new byte[100])
                .publicationDate(2020)
                .description(ra(8)),
            400,
            "Missing or invalid: authors"),
        new FailedArgs(
            "Too many authors",
            new UploadBookRequest()
                .title(ra(10))
                .isbn(ra(13))
                .description(ra(8))
                .publicationDate(2020)
                .content(new byte[100])
                .authors(
                    List.of(
                        Arrays.stream(new int[10]).mapToObj(e -> ra(10)).toArray(String[]::new))),
            400,
            "Missing or invalid: authors"),
        new FailedArgs(
            "Too many tags",
            new UploadBookRequest()
                .title(ra(10))
                .isbn(ra(13))
                .description(ra(8))
                .content(new byte[100])
                .publicationDate(2020)
                .authors(List.of(ra(15)))
                .tags(
                    List.of(
                        Arrays.stream(new int[10]).mapToObj(e -> ra(10)).toArray(String[]::new))),
            400,
            "Missing or invalid: tags"));
  }

  @Test
  void testUploadBook(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    final var request =
        new UploadBookRequest()
            .title(ra(10))
            .isbn(ra(13))
            .description(ra(8))
            .content(new byte[1000])
            .publicationDate(2020)
            .authors(List.of(ra(15)))
            .tags(List.of(Arrays.stream(new int[5]).mapToObj(e -> ra(6)).toArray(String[]::new)));

    final var bookInfo = clientSdk.bookSdk().uploadBook(request);
    assertBookRequest(request, bookInfo);
  }
}
