package io.syebookstore.api.review.repository;

import io.syebookstore.LocalDateTimeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "review")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "book_id")
  private Long bookId;

  @Column(name = "account_id")
  private Long accountId;

  private Integer rating;
  private String message;

  @Column(name = "created_at")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime updatedAt;

  public Long id() {
    return id;
  }

  public Review id(Long id) {
    this.id = id;
    return this;
  }

  public Long bookId() {
    return bookId;
  }

  public Review bookId(Long bookId) {
    this.bookId = bookId;
    return this;
  }

  public Long accountId() {
    return accountId;
  }

  public Review accountId(Long accountId) {
    this.accountId = accountId;
    return this;
  }

  public Integer rating() {
    return rating;
  }

  public Review rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  public String message() {
    return message;
  }

  public Review message(String message) {
    this.message = message;
    return this;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  public Review createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime updatedAt() {
    return updatedAt;
  }

  public Review updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Review.class.getSimpleName() + "[", "]")
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
