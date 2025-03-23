package io.syebookstore.api.book;

import static io.syebookstore.api.account.AccountAssertions.createAccount;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookVocabulary.authors;
import static io.syebookstore.api.book.BookVocabulary.content;
import static io.syebookstore.api.book.BookVocabulary.description;
import static io.syebookstore.api.book.BookVocabulary.isbn;
import static io.syebookstore.api.book.BookVocabulary.publicationDate;
import static io.syebookstore.api.book.BookVocabulary.tags;
import static io.syebookstore.api.book.BookVocabulary.title;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import java.util.function.Consumer;

public class BookAssertions {

  private BookAssertions() {}

  public static BookInfo createBook(AccountInfo accountInfo, Consumer<UploadBookRequest> consumer) {
    try (final var clientSdk = new ClientSdk()) {
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
