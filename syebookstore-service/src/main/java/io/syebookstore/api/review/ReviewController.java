package io.syebookstore.api.review;

import static io.syebookstore.api.review.ReviewMappers.toReviewInfo;

import io.syebookstore.annotations.Protected;
import io.syebookstore.api.ServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/review")
public class ReviewController {

  private final ReviewService reviewService;

  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping("/createReview")
  @Protected
  public ReviewInfo createReview(
      @RequestAttribute("accountId") Long accountId, @RequestBody CreateReviewRequest request) {
    final var bookId = request.bookId();
    if (bookId == null) {
      throw new ServiceException(400, "Missing or invalid: bookId");
    }

    final var rating = request.rating();
    if (rating == null || rating < 1 || rating > 10) {
      throw new ServiceException(400, "Missing or invalid: rating");
    }

    final var message = request.message();
    if (message == null || message.length() < 8 || message.length() > 200) {
      throw new ServiceException(400, "Missing or invalid: message");
    }

    try {
      return toReviewInfo(reviewService.createReview(request, accountId));
    } catch (ServiceException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ServiceException(500, "Review creation failed");
    }
  }

  @PostMapping("/updateReview")
  @Protected
  public ReviewInfo updateReview(
      @RequestAttribute("accountId") Long accountId, @RequestBody UpdateReviewRequest request) {
    final var id = request.id();
    if (id == null) {
      throw new ServiceException(400, "Missing or invalid: id");
    }

    final var rating = request.rating();
    if (rating == null || rating < 1 || rating > 10) {
      throw new ServiceException(400, "Missing or invalid: rating");
    }

    final var message = request.message();
    if (message == null || message.length() < 8 || message.length() > 200) {
      throw new ServiceException(400, "Missing or invalid: message");
    }

    try {
      return toReviewInfo(reviewService.updateReview(request, accountId));
    } catch (ServiceException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new ServiceException(500, "Review updating failed");
    }
  }

  @PostMapping("/listReviews")
  public ListReviewsResponse listReviews(@RequestBody ListReviewsRequest request) {
    final var keyword = request.keyword();
    if (keyword != null && (keyword.length() < 4 || keyword.length() > 64)) {
      throw new ServiceException(400, "Missing or invalid: keyword");
    }

    final var offset = request.offset();
    if (offset != null && offset < 0) {
      throw new ServiceException(400, "Missing or invalid: offset");
    }

    final var limit = request.limit();
    if (limit != null && (limit < 0 || limit > 50)) {
      throw new ServiceException(400, "Missing or invalid: limit");
    }

    final var reviewPage = reviewService.listReviews(request);

    return new ListReviewsResponse()
        .reviewInfos(reviewPage.getContent().stream().map(ReviewMappers::toReviewInfo).toList())
        .limit(request.limit())
        .offset(offset)
        .totalCount(reviewPage.getTotalElements());
  }
}
