package io.syebookstore.api.review;

import io.syebookstore.api.review.repository.Review;

public class ReviewMappers {

  private ReviewMappers() {}

  public static ReviewInfo toReviewInfo(Review review) {
    return new ReviewInfo()
        .id(review.id())
        .bookId(review.bookId())
        .accountId(review.accountId())
        .rating(review.rating())
        .message(review.message())
        .createdAt(review.createdAt())
        .updatedAt(review.updatedAt());
  }
}
