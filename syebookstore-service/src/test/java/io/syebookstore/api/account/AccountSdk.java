package io.syebookstore.api.account;

public interface AccountSdk {

  AccountInfo createAccount(CreateAccountRequest request);

  String login(LoginRequest loginRequest);

  AccountInfo updateAccount(UpdateAccountRequest request);

  AccountInfo getAccount(Long accountId);

  Void deleteAccount(String password);

}
