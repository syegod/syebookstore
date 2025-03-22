package io.syebookstore.api.account;

public interface AccountSdk {

  AccountInfo createAccount(CreateAccountRequest request);

  String login(LoginRequest loginRequest);

  AccountInfo getAccount(Long accountId);

  AccountInfo updateAccount(UpdateAccountRequest request);

}
