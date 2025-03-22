package io.syebookstore.api.review;

import io.syebookstore.api.ServiceException;
import io.syebookstore.api.book.repository.Book;
import io.syebookstore.api.book.repository.BookRepository;
import io.syebookstore.api.review.repository.Review;
import io.syebookstore.api.review.repository.ReviewRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

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

    return reviewRepository.save(
        new Review()
            .bookId(request.bookId())
            .accountId(accountId)
            .rating(request.rating())
            .message(request.message())
            .createdAt(now)
            .updatedAt(now));
  }
}
