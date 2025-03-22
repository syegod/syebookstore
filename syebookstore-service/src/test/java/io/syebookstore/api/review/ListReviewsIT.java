package io.syebookstore.api.review;

import static io.syebookstore.AssertionUtils.assertCollections;
import static io.syebookstore.AssertionUtils.getFields;
import static io.syebookstore.AssertionUtils.toComparator;
import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static io.syebookstore.environment.IntegrationEnvironment.cleanTables;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import io.syebookstore.ClientSdk;
import io.syebookstore.OffsetLimit;
import io.syebookstore.api.Direction;
import io.syebookstore.api.OrderBy;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class ListReviewsIT {

  @BeforeEach
  void beforeEach(DataSource dataSource) {
    cleanTables(dataSource);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testListReviewsFailedMethodSource")
  void testListReviewsFailed(FailedArgs args, ClientSdk clientSdk) {
    try {
      clientSdk.reviewSdk().listReviews(args.request);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, args.errorCode, args.errorMessage);
    }
  }

  private record FailedArgs(
      String test, ListReviewsRequest request, int errorCode, String errorMessage) {
    @Override
    public String toString() {
      return test;
    }
  }

  static Stream<FailedArgs> testListReviewsFailedMethodSource() {
    return Stream.of(
        new FailedArgs(
            "Keyword is too short",
            new ListReviewsRequest().keyword(randomAlphanumeric(3)),
            400,
            "Missing or invalid: keyword"),
        new FailedArgs(
            "Keyword is too long",
            new ListReviewsRequest().keyword(randomAlphanumeric(65)),
            400,
            "Missing or invalid: keyword"),
        new FailedArgs(
            "Offset is negative",
            new ListReviewsRequest().offset(-50),
            400,
            "Missing or invalid: offset"),
        new FailedArgs(
            "Limit is negative",
            new ListReviewsRequest().limit(-50),
            400,
            "Missing or invalid: limit"),
        new FailedArgs(
            "Limit is over than max",
            new ListReviewsRequest().limit(60),
            400,
            "Missing or invalid: limit"));
  }

  @SuppressWarnings("unchecked")
  @ParameterizedTest(name = "{0}")
  @MethodSource("testListReviewsMethodSource")
  void testListReviews(SuccessArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
    final var bookInfo = createBook(accountInfo);
    int n = 25;
    final var request = args.request;
    final var keyword = request.keyword();
    final var offset = request.offset() != null ? request.offset() : 0;
    final var limit = request.limit() != null ? request.limit() : 50;

    login(clientSdk, accountInfo);

    final var reviewInfos =
        IntStream.range(0, n)
            .mapToObj(
                elem ->
                    clientSdk
                        .reviewSdk()
                        .createReview(
                            new CreateReviewRequest()
                                .bookId(bookInfo.id())
                                .rating(5)
                                .message("message@" + n)))
            .filter(reviewInfo -> keyword == null || reviewInfo.message().contains(keyword))
            .sorted(args.comparator)
            .toList();

    final var expectedReviewInfos = reviewInfos.stream().skip(offset).limit(limit).toList();

    final var response = clientSdk.reviewSdk().listReviews(args.request);
    assertEquals(offset, response.offset(), "offset");
    assertEquals(limit, response.limit(), "limit");
    assertEquals(reviewInfos.size(), response.totalCount(), "totalCount");
    assertCollections(expectedReviewInfos, response.reviewInfos(), ReviewAssertions::assertReview);
  }

  @SuppressWarnings("rawtypes")
  private record SuccessArgs(String test, ListReviewsRequest request, Comparator comparator) {
    @Override
    public String toString() {
      return test;
    }
  }

  static Stream<SuccessArgs> testListReviewsMethodSource() {
    final var builder = Stream.<SuccessArgs>builder();

    final String[] fields =
        Arrays.stream(getFields(ReviewInfo.class)).map(Field::getName).toArray(String[]::new);
    final Direction[] directions = {Direction.ASC, Direction.DESC, null};

    // Sort by fields

    for (String field : fields) {
      for (Direction direction : directions) {
        final var orderBy = new OrderBy().field(field).direction(direction);
        builder.add(
            new SuccessArgs(
                "Field: " + field + ", direction: " + direction,
                new ListReviewsRequest().orderBy(orderBy),
                toComparator(orderBy)));
      }
    }

    // Filter by keyword

    final String[] keywords = {"message@", "message@1", null};
    for (String keyword : keywords) {
      builder.add(
          new SuccessArgs(
              "Keyword: " + keyword,
              new ListReviewsRequest().keyword(keyword),
              Comparator.<ReviewInfo, Long>comparing(ReviewInfo::id)));
    }

    // Pagination

    final OffsetLimit[] offsetLimits = {
      new OffsetLimit(null, null),
      new OffsetLimit(null, 5),
      new OffsetLimit(10, null),
      new OffsetLimit(5, 10),
      new OffsetLimit(10, 5),
      new OffsetLimit(20, 10)
    };

    for (OffsetLimit offsetLimit : offsetLimits) {
      final var offset = offsetLimit.offset();
      final var limit = offsetLimit.limit();
      builder.add(
          new SuccessArgs(
              "Offset: " + offset + ", limit: " + limit,
              new ListReviewsRequest().offset(offset).limit(limit),
              Comparator.<ReviewInfo, Long>comparing(ReviewInfo::id)));
    }

    return builder.build();
  }
}
