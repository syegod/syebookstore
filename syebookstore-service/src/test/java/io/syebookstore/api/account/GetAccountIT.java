package io.syebookstore.api.account;

import static io.syebookstore.api.ErrorAssertions.assertError;
import static io.syebookstore.api.account.AccountAssertions.assertAccount;
import static io.syebookstore.api.account.AccountAssertions.login;
import static org.junit.jupiter.api.Assertions.fail;

import io.syebookstore.ClientSdk;
import io.syebookstore.environment.IntegrationEnvironmentExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IntegrationEnvironmentExtension.class)
public class GetAccountIT {

  @Test
  void testGetAccountNotLoggedIn(ClientSdk clientSdk) {
    try {
      clientSdk.accountSdk().getAccount(null);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 403, "Not authenticated");
    }
  }

  @Test
  void testGetAccountNonExisting(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    try {
      clientSdk.accountSdk().getAccount(Long.MAX_VALUE);
      fail("Expected exception");
    } catch (Exception ex) {
      assertError(ex, 404, "Account not found");
    }
  }

  @Test
  void testGetAccountOwn(ClientSdk clientSdk, AccountInfo accountInfo) {
    login(clientSdk, accountInfo);
    final var responseAccountInfo = clientSdk.accountSdk().getAccount(null);

    assertAccount(accountInfo, responseAccountInfo);
  }

  @Test
  void testGetAccount(
      ClientSdk clientSdk, AccountInfo accountInfo, AccountInfo anotherAccountInfo) {
    login(clientSdk, accountInfo);
    final var responseAccountInfo = clientSdk.accountSdk().getAccount(anotherAccountInfo.id());

    assertAccount(anotherAccountInfo, responseAccountInfo);
  }
}
