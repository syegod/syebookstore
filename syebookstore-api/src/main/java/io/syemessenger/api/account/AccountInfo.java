package io.syemessenger.api.account;

import java.util.StringJoiner;

public class AccountInfo {

  private String username;
  private String email;

  public String username() {
    return username;
  }

  public AccountInfo username(String username) {
    this.username = username;
    return this;
  }

  public String email() {
    return email;
  }

  public AccountInfo email(String email) {
    this.email = email;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AccountInfo.class.getSimpleName() + "[", "]")
        .add("username='" + username + "'")
        .add("email='" + email + "'")
        .toString();
  }
}
