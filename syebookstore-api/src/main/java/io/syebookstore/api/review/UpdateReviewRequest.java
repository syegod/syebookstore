package io.syebookstore.api.review;

import java.util.StringJoiner;

public class UpdateReviewRequest {

  private Long id;
  private Integer rating;
  private String message;

  public Long id() {
    return id;
  }

  public UpdateReviewRequest id(Long id) {
    this.id = id;
    return this;
  }

  public Integer rating() {
    return rating;
  }

  public UpdateReviewRequest rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  public String message() {
    return message;
  }

  public UpdateReviewRequest message(String message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", UpdateReviewRequest.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("rating=" + rating)
        .add("message='" + message + "'")
        .toString();
  }
}
