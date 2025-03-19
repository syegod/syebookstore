package io.syebookstore.api.account;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class AccountInfo {

  private Long id;
  private String username;
  private String email;
  private AccountStatus status;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

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

  public AccountStatus status() {
    return status;
  }

  public AccountInfo status(AccountStatus status) {
    this.status = status;
    return this;
  }

  public String description() {
    return description;
  }

  public AccountInfo description(String description) {
    this.description = description;
    return this;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  public AccountInfo createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime updatedAt() {
    return updatedAt;
  }

  public AccountInfo updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AccountInfo.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("username='" + username + "'")
        .add("email='" + email + "'")
        .add("status=" + status)
        .add("description='" + description + "'")
        .add("createdAt=" + createdAt)
        .add("updatedAt=" + updatedAt)
        .toString();
  }
}
