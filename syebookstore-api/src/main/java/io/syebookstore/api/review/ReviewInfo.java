package io.syebookstore.api.review;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class ReviewInfo {

  private Long id;
  private Long bookId;
  private Long accountId;
  private Integer rating;
  private String message;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Long id() {
    return id;
  }

  public ReviewInfo id(Long id) {
    this.id = id;
    return this;
  }

  public Long bookId() {
    return bookId;
  }

  public ReviewInfo bookId(Long bookId) {
    this.bookId = bookId;
    return this;
  }

  public Long accountId() {
    return accountId;
  }

  public ReviewInfo accountId(Long accountId) {
    this.accountId = accountId;
    return this;
  }

  public Integer rating() {
    return rating;
  }

  public ReviewInfo rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  public String message() {
    return message;
  }

  public ReviewInfo message(String message) {
    this.message = message;
    return this;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  public ReviewInfo createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime updatedAt() {
    return updatedAt;
  }

  public ReviewInfo updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ReviewInfo.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("bookId=" + bookId)
        .add("accountId=" + accountId)
        .add("rating=" + rating)
        .add("message='" + message + "'")
        .add("createdAt=" + createdAt)
        .add("updatedAt=" + updatedAt)
        .toString();
  }
}
