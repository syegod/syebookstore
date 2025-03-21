package io.syebookstore.api.book;

import io.syebookstore.api.OrderBy;
import java.util.List;
import java.util.StringJoiner;

public class ListBooksRequest {
  private String keyword;
  private List<String> tags;
  private Integer limit;
  private Integer offset;
  private OrderBy orderBy;

  public String keyword() {
    return keyword;
  }

  public ListBooksRequest keyword(String keyword) {
    this.keyword = keyword;
    return this;
  }

  public List<String> tags() {
    return tags;
  }

  public ListBooksRequest tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public Integer limit() {
    return limit;
  }

  public ListBooksRequest limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public Integer offset() {
    return offset;
  }

  public ListBooksRequest offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public OrderBy orderBy() {
    return orderBy;
  }

  public ListBooksRequest orderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ListBooksRequest.class.getSimpleName() + "[", "]")
        .add("keyword='" + keyword + "'")
        .add("tags=" + tags)
        .add("limit=" + limit)
        .add("offset=" + offset)
        .add("orderBy=" + orderBy)
        .toString();
  }
}
