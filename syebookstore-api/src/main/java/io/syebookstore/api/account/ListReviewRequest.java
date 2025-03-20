package io.syebookstore.api.account;

import io.syebookstore.api.OrderBy;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class ListReviewRequest {

  private String keyword;
  private Integer fromRating;
  private Integer toRating;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
  private Integer limit;
  private Integer offset;
  private OrderBy orderBy;

  public String keyword() {
    return keyword;
  }

  public ListReviewRequest keyword(String keyword) {
    this.keyword = keyword;
    return this;
  }

  public Integer fromRating() {
    return fromRating;
  }

  public ListReviewRequest fromRating(Integer fromRating) {
    this.fromRating = fromRating;
    return this;
  }

  public Integer toRating() {
    return toRating;
  }

  public ListReviewRequest toRating(Integer toRating) {
    this.toRating = toRating;
    return this;
  }

  public LocalDateTime fromDate() {
    return fromDate;
  }

  public ListReviewRequest fromDate(LocalDateTime fromDate) {
    this.fromDate = fromDate;
    return this;
  }

  public LocalDateTime toDate() {
    return toDate;
  }

  public ListReviewRequest toDate(LocalDateTime toDate) {
    this.toDate = toDate;
    return this;
  }

  public Integer limit() {
    return limit;
  }

  public ListReviewRequest limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public Integer offset() {
    return offset;
  }

  public ListReviewRequest offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public OrderBy orderBy() {
    return orderBy;
  }

  public ListReviewRequest orderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ListReviewRequest.class.getSimpleName() + "[", "]")
        .add("keyword='" + keyword + "'")
        .add("fromRating=" + fromRating)
        .add("toRating=" + toRating)
        .add("fromDate=" + fromDate)
        .add("toDate=" + toDate)
        .add("limit=" + limit)
        .add("offset=" + offset)
        .add("orderBy=" + orderBy)
        .toString();
  }
}
