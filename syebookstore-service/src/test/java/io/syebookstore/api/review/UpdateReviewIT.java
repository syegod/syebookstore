package io.syebookstore.api.review;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static io.syebookstore.api.review.ReviewAssertions.assertReview;
import static io.syebookstore.api.review.ReviewAssertions.createReview;
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
public class UpdateReviewIT {

  private static BookInfo existingBookInfo;
  private static ReviewInfo existingReviewInfo;

  @BeforeAll
  static void beforeAll() {
    existingBookInfo = createBook();
    existingReviewInfo = createReview(existingBookInfo);
  }

  @Test
  void testUpdateReviewNotLoggedIn(ClientSdk clientSdk, AccountInfo accountInfo) {
    try {
      clientSdk.reviewSdk().updateReview(new UpdateReviewRequest());
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 403, "Not authenticated");
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testUpdateReviewFailedMethodSource")
  void testUpdateReviewFailed(FailedArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    try {
      clientSdk.reviewSdk().updateReview(args.request);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, args.errorCode, args.errorMessage);
    }
  }

  private record FailedArgs(
      String test, UpdateReviewRequest request, int errorCode, String errorMessage) {
    @Override
    public String toString() {
      return test;
    }
  }

  static Stream<FailedArgs> testUpdateReviewFailedMethodSource() {
    return Stream.of(
        new FailedArgs("No review id", new UpdateReviewRequest(), 400, "Missing or invalid: id"),
        new FailedArgs(
            "No rating",
            new UpdateReviewRequest().id(existingReviewInfo.id()),
            400,
            "Missing or invalid: rating"),
        new FailedArgs(
            "Rating less than min",
            new UpdateReviewRequest().id(existingReviewInfo.id()).rating(0),
            400,
            "Missing or invalid: rating"),
        new FailedArgs(
            "Rating over than max",
            new UpdateReviewRequest().id(existingReviewInfo.id()).rating(11),
            400,
            "Missing or invalid: rating"),
        new FailedArgs(
            "No message",
            new UpdateReviewRequest().id(existingReviewInfo.id()).rating(5),
            400,
            "Missing or invalid: message"),
        new FailedArgs(
            "Message too short",
            new UpdateReviewRequest()
                .id(existingReviewInfo.id())
                .rating(5)
                .message(randomAlphanumeric(7)),
            400,
            "Missing or invalid: message"),
        new FailedArgs(
            "Message too long",
            new UpdateReviewRequest()
                .id(existingReviewInfo.id())
                .rating(5)
                .message(randomAlphanumeric(201)),
            400,
            "Missing or invalid: message"),
        new FailedArgs(
            "Review not exists",
            new UpdateReviewRequest().id(Long.MAX_VALUE).rating(5).message(randomAlphanumeric(20)),
            404,
            "Review not found"),
        new FailedArgs(
            "Not a review author",
            new UpdateReviewRequest()
                .id(existingReviewInfo.id())
                .rating(5)
                .message(randomAlphanumeric(20)),
            403,
            "Not a review author"));
  }

  @Test
  void testUpdateReview(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    final var reviewInfo = createReview(existingBookInfo, accountInfo);

    final var rating = 5;
    final var message = randomAlphanumeric(20);
    final var updatedReviewInfo =
        clientSdk
            .reviewSdk()
            .updateReview(
                new UpdateReviewRequest().id(reviewInfo.id()).rating(rating).message(message));

    assertReview(reviewInfo, updatedReviewInfo, ri -> ri.rating(rating).message(message));
  }
}
