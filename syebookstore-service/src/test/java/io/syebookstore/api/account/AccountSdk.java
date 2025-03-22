package io.syebookstore.api.account;

public interface AccountSdk {

  AccountInfo createAccount(CreateAccountRequest request);

  String login(LoginRequest loginRequest);

  AccountInfo getAccount(Long accountId);

  AccountInfo updateAccount(UpdateAccountRequest request);

  // TODO: https://github.com/syegod/syebookstore/issues/2
  ListReviewResponse listReviews(ListReviewRequest request);
}
