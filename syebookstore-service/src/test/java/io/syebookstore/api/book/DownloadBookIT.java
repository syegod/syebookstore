package io.syebookstore.api.book;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.login;
import static io.syebookstore.api.book.BookAssertions.createBook;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

import io.syebookstore.ClientSdk;
import io.syebookstore.api.account.AccountInfo;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class DownloadBookIT {

  @Test
  void testDownloadBookNotLoggedIn(ClientSdk clientSdk) {
    try {
      clientSdk.bookSdk().downloadBook(Long.MAX_VALUE);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 403, "Not authenticated");
    }
  }

  @Test
  void testDownloadBookNotExisting(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    try {
      clientSdk.bookSdk().downloadBook(Long.MAX_VALUE);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 404, "Book not found");
    }
  }

  @Test
  void testDownloadBook(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    final var bookInfo = createBook(accountInfo);

    final var bookContent = clientSdk.bookSdk().downloadBook(bookInfo.id());

    assertArrayEquals(bookInfo.content(), bookContent);
  }
}
