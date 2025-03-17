package io.syebookstore.api.account;

import io.syemessenger.api.account.AccountInfo;
import io.syemessenger.api.account.CreateAccountRequest;
import io.syemessenger.api.account.LoginRequest;
import io.syemessenger.api.account.UpdateAccountRequest;

public interface AccountSdk {

  String createAccount(CreateAccountRequest request);

  String login(LoginRequest loginRequest);

  AccountInfo updateAccount(UpdateAccountRequest request);

  AccountInfo getAccount(Long accountId);

  Void deleteAccount(String password);

}
