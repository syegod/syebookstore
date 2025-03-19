package io.syebookstore.api.account;

import io.syebookstore.api.account.repository.Account;

public class AccountMappers {

  private AccountMappers() {}

  public static AccountInfo toAccountInfo(Account account) {
    return new AccountInfo()
        .id(account.id())
        .username(account.username())
        .email(account.email())
        .description(account.description())
        .status(account.status())
        .createdAt(account.createdAt())
        .updatedAt(account.updatedAt());
  }
}
