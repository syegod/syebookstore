package io.syebookstore.api.review;

import io.syebookstore.api.OrderBy;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class ListReviewsRequest {

  private Long bookId;
  private Long accountId;
  private String keyword;
  private Integer fromRating;
  private Integer toRating;
  private LocalDateTime fromDateTime;
  private LocalDateTime toDateTime;
  private Integer offset;
  private Integer limit;
  private OrderBy orderBy;

  public Long bookId() {
    return bookId;
  }

  public ListReviewsRequest bookId(Long bookId) {
    this.bookId = bookId;
    return this;
  }

  public Long accountId() {
    return accountId;
  }

  public ListReviewsRequest accountId(Long accountId) {
    this.accountId = accountId;
    return this;
  }

  public String keyword() {
    return keyword;
  }

  public ListReviewsRequest keyword(String keyword) {
    this.keyword = keyword;
    return this;
  }

  public Integer fromRating() {
    return fromRating;
  }

  public ListReviewsRequest fromRating(Integer fromRating) {
    this.fromRating = fromRating;
    return this;
  }

  public Integer toRating() {
    return toRating;
  }

  public ListReviewsRequest toRating(Integer toRating) {
    this.toRating = toRating;
    return this;
  }

  public LocalDateTime fromDateTime() {
    return fromDateTime;
  }

  public ListReviewsRequest fromDateTime(LocalDateTime fromDateTime) {
    this.fromDateTime = fromDateTime;
    return this;
  }

  public LocalDateTime toDateTime() {
    return toDateTime;
  }

  public ListReviewsRequest toDateTime(LocalDateTime toDateTime) {
    this.toDateTime = toDateTime;
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
        .add("accountId=" + accountId)
        .add("keyword='" + keyword + "'")
        .add("fromRating=" + fromRating)
        .add("toRating=" + toRating)
        .add("fromDateTime=" + fromDateTime)
        .add("toDateTime=" + toDateTime)
        .add("offset=" + offset)
        .add("limit=" + limit)
        .add("orderBy=" + orderBy)
        .toString();
  }
}
