package io.syebookstore.api.review;

import io.syebookstore.api.OrderBy;
import java.util.StringJoiner;

public class ListReviewsRequest {

  private Long bookId;
  private String keyword;
  private Integer offset = 0;
  private Integer limit = 50;
  private OrderBy orderBy;

  public Long bookId() {
    return bookId;
  }

  public ListReviewsRequest bookId(Long bookId) {
    this.bookId = bookId;
    return this;
  }

  public String keyword() {
    return keyword;
  }

  public ListReviewsRequest keyword(String keyword) {
    this.keyword = keyword;
    return this;
  }

  public Integer offset() {
    return offset;
  }

  public ListReviewsRequest offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public Integer limit() {
    return limit;
  }

  public ListReviewsRequest limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public OrderBy orderBy() {
    return orderBy;
  }

  public ListReviewsRequest orderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ListReviewsRequest.class.getSimpleName() + "[", "]")
        .add("bookId=" + bookId)
        .add("keyword='" + keyword + "'")
        .add("offset=" + offset)
        .add("limit=" + limit)
        .add("orderBy=" + orderBy)
        .toString();
  }
}
