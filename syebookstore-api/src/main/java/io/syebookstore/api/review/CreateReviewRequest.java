package io.syebookstore.api.review;

import java.util.StringJoiner;

public class CreateReviewRequest {

  private Long bookId;
  private Integer rating;
  private String message;

  public Long bookId() {
    return bookId;
  }

  public CreateReviewRequest bookId(Long bookId) {
    this.bookId = bookId;
    return this;
  }

  public Integer rating() {
    return rating;
  }

  public CreateReviewRequest rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  public String message() {
    return message;
  }

  public CreateReviewRequest message(String message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", CreateReviewRequest.class.getSimpleName() + "[", "]")
        .add("bookId=" + bookId)
        .add("rating=" + rating)
        .add("message='" + message + "'")
        .toString();
  }
}
