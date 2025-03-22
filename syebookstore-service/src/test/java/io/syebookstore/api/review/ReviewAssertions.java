package io.syebookstore.api.review;

import static io.syebookstore.api.account.AccountAssertions.createAccount;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.api.book.BookInfo;
import java.util.function.Consumer;

public class ReviewAssertions {

  private ReviewAssertions() {}

  public static ReviewInfo createReview() {
    return createReview(createBook());
  }

  public static ReviewInfo createReview(BookInfo bookInfo) {
    return createReview(bookInfo, createAccount());
  }

  public static ReviewInfo createReview(BookInfo bookInfo, AccountInfo accountInfo) {
    try (final var clientSdk = new ClientSdk()) {
      login(clientSdk, accountInfo);
      return clientSdk
          .reviewSdk()
          .createReview(
              new CreateReviewRequest()
                  .bookId(bookInfo.id())
                  .rating(10)
                  .message(randomAlphanumeric(10)));
    }
  }

  public static void assertReview(ReviewInfo expected, ReviewInfo actual) {
    assertReview(expected, actual, null);
  }

  public static void assertReview(
      ReviewInfo expected, ReviewInfo actual, Consumer<ReviewInfo> expectedConsumer) {
    if (expectedConsumer != null) {
      expectedConsumer.accept(expected);
    }
    assertEquals(expected.id(), actual.id(), "actual.id");
    assertEquals(expected.bookId(), actual.bookId(), "actual.bookId");
    assertEquals(expected.accountId(), actual.accountId(), "actual.accountId");
    assertEquals(expected.rating(), actual.rating(), "actual.rating");
    assertEquals(expected.message(), actual.message(), "actual.message");
    assertEquals(expected.createdAt(), actual.createdAt(), "actual.createdAt");
    assertNotNull(actual.updatedAt(), "actual.updatedAt");
  }
}
