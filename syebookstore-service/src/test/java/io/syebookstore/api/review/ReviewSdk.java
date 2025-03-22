package io.syebookstore.api.review;

public interface ReviewSdk {

  ReviewInfo createReview(CreateReviewRequest request);

  ReviewInfo updateReview(UpdateReviewRequest request);

  ListReviewsResponse listReview(ListReviewsRequest request);
}
