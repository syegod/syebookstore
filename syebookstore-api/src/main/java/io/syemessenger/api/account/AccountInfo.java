package io.syemessenger.api.account;

import java.util.StringJoiner;

public class AccountInfo {

  private Long id;
  private String username;
  private String email;

  public Long id() {
    return id;
  }

  public AccountInfo id(Long id) {
    this.id = id;
    return this;
  }

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
        .add("id=" + id)
        .add("username='" + username + "'")
        .add("email='" + email + "'")
        .toString();
  }
}
