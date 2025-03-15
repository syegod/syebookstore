package io.syebookstore.api.account;

import io.syemessenger.api.account.AccountInfo;
import io.syemessenger.api.account.CreateAccountRequest;
import io.syemessenger.api.account.LoginRequest;
import io.syemessenger.api.account.UpdateAccountRequest;

public interface AccountSdk {

  String createAccount(CreateAccountRequest createAccountRequest);

  String login(LoginRequest loginRequest);

  AccountInfo updateAccount(UpdateAccountRequest updateAccountRequest);

  AccountInfo getAccount(Long accountId);
}
