package io.syebookstore.api.book;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookAssertions.assertBookRequest;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static io.syebookstore.api.book.BookVocabulary.authors;
import static io.syebookstore.api.book.BookVocabulary.content;
import static io.syebookstore.api.book.BookVocabulary.description;
import static io.syebookstore.api.book.BookVocabulary.isbn;
import static io.syebookstore.api.book.BookVocabulary.publicationDate;
import static io.syebookstore.api.book.BookVocabulary.tags;
import static io.syebookstore.api.book.BookVocabulary.title;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

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
  @MethodSource("testUploadBookFailedMethodSource")
  void testUploadBookFailed(FailedArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
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

  static Stream<FailedArgs> testUploadBookFailedMethodSource() {
    return Stream.of(
        new FailedArgs("Empty request", new UploadBookRequest(), 400, "Missing or invalid: title"),
        new FailedArgs(
            "Title too short",
            new UploadBookRequest().title(randomAlphanumeric(7)),
            400,
            "Missing or invalid: title"),
        new FailedArgs(
            "Title too long",
            new UploadBookRequest().title(randomAlphanumeric(65)),
            400,
            "Missing or invalid: title"),
        new FailedArgs(
            "No isbn", new UploadBookRequest().title(title()), 400, "Missing or invalid: isbn"),
        new FailedArgs(
            "Isbn is not 13 char length",
            new UploadBookRequest().title(title()).isbn(randomAlphanumeric(10)),
            400,
            "Missing or invalid: isbn"),
        new FailedArgs(
            "No description",
            new UploadBookRequest().title(title()).isbn(isbn()),
            400,
            "Missing or invalid: description"),
        new FailedArgs(
            "Description too short",
            new UploadBookRequest().title(title()).isbn(isbn()).description(randomAlphanumeric(7)),
            400,
            "Missing or invalid: description"),
        new FailedArgs(
            "Description too long",
            new UploadBookRequest()
                .title(title())
                .isbn(isbn())
                .description(randomAlphanumeric(501)),
            400,
            "Missing or invalid: description"),
        new FailedArgs(
            "No publication date",
            new UploadBookRequest().title(title()).isbn(isbn()).description(description()),
            400,
            "Missing or invalid: publication date"),
        new FailedArgs(
            "Publication date is in future",
            new UploadBookRequest()
                .title(title())
                .isbn(isbn())
                .description(description())
                .publicationDate(2030),
            400,
            "Missing or invalid: publication date"),
        new FailedArgs(
            "No content",
            new UploadBookRequest()
                .title(title())
                .isbn(isbn())
                .description(description())
                .publicationDate(publicationDate()),
            400,
            "Missing or invalid: content"),
        new FailedArgs(
            "No authors",
            new UploadBookRequest()
                .title(title())
                .isbn(isbn())
                .description(description())
                .publicationDate(publicationDate())
                .content(content()),
            400,
            "Missing or invalid: authors"),
        new FailedArgs(
            "Too many authors",
            new UploadBookRequest()
                .title(title())
                .isbn(isbn())
                .description(description())
                .publicationDate(publicationDate())
                .content(content())
                .authors(
                    List.of(
                        Arrays.stream(new int[10])
                            .mapToObj(e -> randomAlphanumeric(10))
                            .toArray(String[]::new))),
            400,
            "Missing or invalid: authors"),
        new FailedArgs(
            "No tags",
            new UploadBookRequest()
                .title(title())
                .isbn(isbn())
                .description(description())
                .publicationDate(publicationDate())
                .content(content())
                .authors(authors()),
            400,
            "Missing or invalid: tags"),
        new FailedArgs(
            "Too many tags",
            new UploadBookRequest()
                .title(title())
                .isbn(isbn())
                .description(description())
                .content(content())
                .publicationDate(publicationDate())
                .authors(authors())
                .tags(
                    List.of(
                        Arrays.stream(new int[10])
                            .mapToObj(e -> randomAlphanumeric(10))
                            .toArray(String[]::new))),
            400,
            "Missing or invalid: tags"));
  }

  @Test
  void testUploadBookExistingISBN(AccountInfo accountInfo) {
    final var isbn = isbn();
    createBook(accountInfo, request -> request.isbn(isbn));
    try {
      createBook(accountInfo, request -> request.isbn(isbn));
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 400, "Cannot upload book: already exists");
    }
  }

  @Test
  void testUploadBook(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    final var request =
        new UploadBookRequest()
            .title(title())
            .isbn(isbn())
            .description(description())
            .content(content())
            .publicationDate(publicationDate())
            .authors(authors())
            .tags(tags());

    final var bookInfo = clientSdk.bookSdk().uploadBook(request);
    assertBookRequest(request, bookInfo);
  }
}
