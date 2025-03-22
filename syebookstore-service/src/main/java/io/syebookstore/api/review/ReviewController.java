package io.syebookstore.api.review;

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
  }
}
