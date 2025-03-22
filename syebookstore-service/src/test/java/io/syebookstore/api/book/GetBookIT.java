package io.syebookstore.api.book;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.book.BookAssertions.assertBook;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static org.junit.jupiter.api.Assertions.fail;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class GetBookIT {

  @Test
  void testGetBookNotExisting(ClientSdk clientSdk) {
    try {
      clientSdk.bookSdk().getBook(Long.MAX_VALUE);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 404, "Book not found");
    }
  }

  @Test
  void testGetBook(ClientSdk clientSdk, AccountInfo accountInfo) {
    final var expectedBookInfo = createBook(accountInfo);

    final var actualBookInfo = clientSdk.bookSdk().getBook(expectedBookInfo.id());

    assertBook(expectedBookInfo, actualBookInfo);
  }
}
