package io.syebookstore.api.review;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.api.book.BookInfo;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class CreateReviewIT {

  private static BookInfo existingBookInfo;

  @BeforeAll
  static void beforeAll() {
    existingBookInfo = createBook();
  }

  @Test
  void testCreateReviewNotLoggedIn(ClientSdk clientSdk, AccountInfo accountInfo) {
    try {
      clientSdk.reviewSdk().createReview(new CreateReviewRequest());
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 403, "Not authenticated");
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testCreateReviewFailedMethodSource")
  void testCreateReviewFailed(FailedArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    try {
      clientSdk.reviewSdk().createReview(args.request);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, args.errorCode, args.errorMessage);
    }
  }

  private record FailedArgs(
      String test, CreateReviewRequest request, int errorCode, String errorMessage) {
    @Override
    public String toString() {
      return test;
    }
  }

  static Stream<FailedArgs> testCreateReviewFailedMethodSource() {
    return Stream.of(
        new FailedArgs("No book id", new CreateReviewRequest(), 400, "Missing or invalid: bookId"),
        new FailedArgs(
            "No rating",
            new CreateReviewRequest().bookId(existingBookInfo.id()),
            400,
            "Missing or invalid: rating"),
        new FailedArgs(
            "Rating less than min",
            new CreateReviewRequest().bookId(existingBookInfo.id()).rating(0),
            400,
            "Missing or invalid: rating"),
        new FailedArgs(
            "Rating more than max",
            new CreateReviewRequest().bookId(existingBookInfo.id()).rating(11),
            400,
            "Missing or invalid: rating"),
        new FailedArgs(
            "No message",
            new CreateReviewRequest().bookId(existingBookInfo.id()).rating(5),
            400,
            "Missing or invalid: message"),
        new FailedArgs(
            "Message less than min",
            new CreateReviewRequest()
                .bookId(existingBookInfo.id())
                .rating(5)
                .message(randomAlphanumeric(7)),
            400,
            "Missing or invalid: message"),
        new FailedArgs(
            "Message less than min",
            new CreateReviewRequest()
                .bookId(existingBookInfo.id())
                .rating(5)
                .message(randomAlphanumeric(201)),
            400,
            "Missing or invalid: message"),
        new FailedArgs(
            "Book not exists",
            new CreateReviewRequest()
                .bookId(Long.MAX_VALUE)
                .rating(5)
                .message(randomAlphanumeric(200)),
            404,
            "Book not found"));
  }

  @Test
  void testCreateReview(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);

    final var bookId = existingBookInfo.id();
    final var rating = 10;
    final var message = randomAlphanumeric(10);
    final var reviewInfo =
        clientSdk
            .reviewSdk()
            .createReview(new CreateReviewRequest().bookId(bookId).rating(rating).message(message));

    assertEquals(bookId, reviewInfo.bookId(), "reviewInfo.bookId");
    assertEquals(accountInfo.id(), reviewInfo.accountId(), "reviewInfo.accountId");
    assertEquals(rating, reviewInfo.rating(), "reviewInfo.rating");
    assertEquals(message, reviewInfo.message(), "reviewInfo.message");
    assertNotNull(reviewInfo.createdAt());
    assertNotNull(reviewInfo.updatedAt());
  }
}
