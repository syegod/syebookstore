package io.syemessenger.api.account;

import java.util.StringJoiner;

public class LoginRequest {

  private String usernameOrEmail;
  private String password;

  public String usernameOrEmail() {
    return usernameOrEmail;
  }

  public LoginRequest usernameOrEmail(String usernameOrEmail) {
    this.usernameOrEmail = usernameOrEmail;
    return this;
  }

  public String password() {
    return password;
  }

  public LoginRequest password(String password) {
    this.password = password;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", LoginRequest.class.getSimpleName() + "[", "]")
        .add("usernameOrEmail='" + usernameOrEmail + "'")
        .add("password='" + password + "'")
        .toString();
  }
}
