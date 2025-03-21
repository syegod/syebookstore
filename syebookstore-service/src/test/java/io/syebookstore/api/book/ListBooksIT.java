package io.syebookstore.api.book;

import static io.syebookstore.AssertionUtils.assertCollections;
import static io.syebookstore.AssertionUtils.getFields;
import static io.syebookstore.AssertionUtils.toComparator;
import static io.syebookstore.CounterUtils.nextLong;
import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static io.syebookstore.environment.IntegrationEnvironment.cleanTables;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import io.syebookstore.ClientSdk;
import io.syebookstore.OffsetLimit;
import io.syebookstore.api.Direction;
import io.syebookstore.api.OrderBy;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class ListBooksIT {

  @BeforeEach
  void beforeEach(DataSource dataSource) {
    cleanTables(dataSource);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testListBooksFailedMethodSource")
  void testListBooksFailed(FailedArgs args, ClientSdk clientSdk) {
    try {
      clientSdk.bookSdk().listBooks(args.request);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, args.errorCode, args.errorMessage);
    }
  }

  private record FailedArgs(
      String test, ListBooksRequest request, int errorCode, String errorMessage) {
    @Override
    public String toString() {
      return test;
    }
  }

  static Stream<FailedArgs> testListBooksFailedMethodSource() {
    return Stream.of(
        new FailedArgs(
            "Keyword is too short",
            new ListBooksRequest().keyword(randomAlphanumeric(3)),
            400,
            "Missing or invalid: keyword"),
        new FailedArgs(
            "Keyword is too long",
            new ListBooksRequest().keyword(randomAlphanumeric(65)),
            400,
            "Missing or invalid: keyword"),
        new FailedArgs(
            "Too many tags",
            new ListBooksRequest()
                .tags(IntStream.range(0, 20).mapToObj(e -> randomAlphanumeric(10)).toList()),
            400,
            "Missing or invalid: tags"),
        new FailedArgs(
            "Offset is negative",
            new ListBooksRequest().offset(-50),
            400,
            "Missing or invalid: offset"),
        new FailedArgs(
            "Limit is negative",
            new ListBooksRequest().limit(-50),
            400,
            "Missing or invalid: limit"),
        new FailedArgs(
            "Limit is over than max",
            new ListBooksRequest().limit(60),
            400,
            "Missing or invalid: limit"));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testListBooksMethodSource")
  void testListBooks(SuccessArgs args, ClientSdk clientSdk, AccountInfo accountInfo) {
    int n = 25;
    final var request = args.request;
    final var keyword = request.keyword();
    final var offset = request.offset() != null ? request.offset() : 0;
    final var limit = request.limit() != null ? request.limit() : 50;

    final var bookInfos =
        IntStream.range(0, n)
            .mapToObj(
                v -> {
                  var l = nextLong();
                  return createBook(
                      accountInfo, req -> req.title("title@" + l).description("description@" + l));
                })
            .filter(
                bookInfo -> {
                  if (keyword != null) {
                    return bookInfo.title().contains(keyword)
                        || bookInfo.description().contains(keyword);
                  } else {
                    return true;
                  }
                })
            .sorted(args.comparator)
            .toList();

    final var expectedBookInfos = bookInfos.stream().skip(offset).limit(limit).toList();

    final var response = clientSdk.bookSdk().listBooks(request);
    assertEquals(bookInfos.size(), response.totalCount(), "totalCount");
    assertCollections(expectedBookInfos, response.bookInfos(), BookAssertions::assertBook);
  }

  @SuppressWarnings("rawtypes")
  private record SuccessArgs(String test, ListBooksRequest request, Comparator comparator) {
    @Override
    public String toString() {
      return test;
    }
  }

  static Stream<SuccessArgs> testListBooksMethodSource() {
    final var builder = Stream.<SuccessArgs>builder();

    final String[] fields =
        Arrays.stream(getFields(BookInfo.class))
            .filter(f -> !f.getType().isArray() && !f.getType().isAssignableFrom(List.class))
            .map(Field::getName)
            .toArray(String[]::new);
    final Direction[] directions = {Direction.ASC, Direction.DESC, null};

    // Sort by fields

    for (String field : fields) {
      for (Direction direction : directions) {
        final var orderBy = new OrderBy().field(field).direction(direction);
        builder.add(
            new SuccessArgs(
                "Field: " + field + ", direction: " + direction,
                new ListBooksRequest().orderBy(orderBy),
                toComparator(orderBy)));
      }
    }

    // Filter by keyword

    final String[] keywords = {"title@", "description@", null};
    for (String keyword : keywords) {
      builder.add(
          new SuccessArgs(
              "Keyword: " + keyword,
              new ListBooksRequest().keyword(keyword),
              Comparator.<BookInfo, Long>comparing(BookInfo::id)));
    }

    // TODO: https://github.com/syegod/syebookstore/issues/7

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
              new ListBooksRequest().offset(offset).limit(limit),
              Comparator.<BookInfo, Long>comparing(BookInfo::id)));
    }

    return builder.build();
  }
}
