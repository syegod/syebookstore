package io.syebookstore.api.book;

import io.syebookstore.api.OrderBy;
import java.util.List;
import java.util.StringJoiner;

public class ListBooksResponse {
  private Integer limit;
  private Integer offset;
  private OrderBy orderBy;
  private Long totalCount;
  private List<BookInfo> bookInfos;

  public Integer limit() {
    return limit;
  }

  public ListBooksResponse limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public Integer offset() {
    return offset;
  }

  public ListBooksResponse offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public OrderBy orderBy() {
    return orderBy;
  }

  public ListBooksResponse orderBy(OrderBy orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public Long totalCount() {
    return totalCount;
  }

  public ListBooksResponse totalCount(Long totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  public List<BookInfo> bookInfos() {
    return bookInfos;
  }

  public ListBooksResponse bookInfos(List<BookInfo> bookInfos) {
    this.bookInfos = bookInfos;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ListBooksResponse.class.getSimpleName() + "[", "]")
        .add("limit=" + limit)
        .add("offset=" + offset)
        .add("orderBy=" + orderBy)
        .add("totalCount=" + totalCount)
        .add("bookInfos=" + bookInfos)
        .toString();
  }
}
