package io.syebookstore.api.book;

import static io.syebookstore.AssertionUtils.ra;
import static io.syebookstore.api.account.AccountAssertions.createAccount;
import static io.syebookstore.api.account.AccountAssertions.login;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class BookAssertions {

  private BookAssertions() {}

  public static BookInfo createBook(AccountInfo accountInfo, Consumer<UploadBookRequest> consumer) {
    try (final var clientSdk = new ClientSdk()) {
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

      if (consumer != null) {
        consumer.accept(request);
      }

      return clientSdk.bookSdk().uploadBook(request);
    }
  }

  public static BookInfo createBook(AccountInfo accountInfo) {
    return createBook(accountInfo, null);
  }

  public static BookInfo createBook() {
    return createBook(createAccount());
  }

  public static void assertBook(BookInfo expected, BookInfo actual) {
    assertEquals(expected.id(), actual.id(), "actual.id");
    assertEquals(expected.isbn(), actual.isbn(), "actual.isbn");
    assertEquals(expected.title(), actual.title(), "actual.title");
    assertEquals(expected.description(), actual.description(), "actual.description");
    assertEquals(expected.publicationDate(), actual.publicationDate(), "actual.publicationDate");
    assertArrayEquals(expected.content(), actual.content());
    assertEquals(expected.authors(), actual.authors(), "actual.authors");
    assertEquals(expected.tags(), actual.tags(), "actual.tags");
  }

  public static void assertBookRequest(UploadBookRequest expected, BookInfo actual) {
    assertEquals(expected.isbn(), actual.isbn(), "actual.isbn");
    assertEquals(expected.title(), actual.title(), "actual.title");
    assertEquals(expected.description(), actual.description(), "actual.description");
    assertEquals(expected.publicationDate(), actual.publicationDate(), "actual.publicationDate");
    assertArrayEquals(expected.content(), actual.content());
    assertEquals(expected.authors(), actual.authors(), "actual.authors");
    assertEquals(expected.tags(), actual.tags(), "actual.tags");
  }
}
