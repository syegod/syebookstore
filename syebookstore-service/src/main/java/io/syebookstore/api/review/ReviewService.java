package io.syebookstore.api.review;

import static io.syebookstore.api.Pageables.toPageable;

import io.syebookstore.api.ServiceException;
import io.syebookstore.api.book.repository.BookRepository;
import io.syebookstore.api.review.repository.Review;
import io.syebookstore.api.review.repository.ReviewRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

  public static final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);

  private final ReviewRepository reviewRepository;
  private final BookRepository bookRepository;

  public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
    this.reviewRepository = reviewRepository;
    this.bookRepository = bookRepository;
  }

  public Review createReview(CreateReviewRequest request, Long accountId) {
    final var now = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MILLIS);

    final var book = bookRepository.findById(request.bookId()).orElse(null);
    if (book == null) {
      throw new ServiceException(404, "Book not found");
    }

    final var review =
        new Review()
            .bookId(request.bookId())
            .accountId(accountId)
            .rating(request.rating())
            .message(request.message())
            .createdAt(now)
            .updatedAt(now);

    LOGGER.info("Saving review: {}", review);

    return reviewRepository.save(review);
  }

  public Review updateReview(UpdateReviewRequest request, Long accountId) {
    final var now = LocalDateTime.now(Clock.systemUTC()).truncatedTo(ChronoUnit.MILLIS);

    final var review = reviewRepository.findById(request.id()).orElse(null);
    if (review == null) {
      throw new ServiceException(404, "Review not found");
    }

    if (!review.accountId().equals(accountId)) {
      throw new ServiceException(403, "Not a review author");
    }

    review.rating(request.rating()).message(request.message()).updatedAt(now);

    LOGGER.info("Updating review: {}", review);

    return reviewRepository.save(review);
  }

  public Page<Review> listReviews(ListReviewsRequest request) {
    final var pageable = toPageable(request.offset(), request.limit(), request.orderBy());

    final var reviewPage =
        reviewRepository.findByBookIdAndMessageContaining(
            request.bookId(), request.keyword(), pageable);

    LOGGER.info("Getting review list: {}", reviewPage);

    return reviewPage;
  }
}
