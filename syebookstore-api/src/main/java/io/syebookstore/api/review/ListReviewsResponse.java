package io.syebookstore.api.review;

import java.util.List;
import java.util.StringJoiner;

public class ListReviewsResponse {

  private Integer offset;
  private Integer limit;
  private Long totalCount;
  private List<ReviewInfo> reviewInfos;

  public Integer offset() {
    return offset;
  }

  public ListReviewsResponse offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  public Integer limit() {
    return limit;
  }

  public ListReviewsResponse limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  public Long totalCount() {
    return totalCount;
  }

  public ListReviewsResponse totalCount(Long totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  public List<ReviewInfo> reviewInfos() {
    return reviewInfos;
  }

  public ListReviewsResponse reviewInfos(List<ReviewInfo> reviewInfos) {
    this.reviewInfos = reviewInfos;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ListReviewsResponse.class.getSimpleName() + "[", "]")
        .add("offset=" + offset)
        .add("limit=" + limit)
        .add("totalCount=" + totalCount)
        .add("reviewInfos=" + reviewInfos)
        .toString();
  }
}
