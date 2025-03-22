package io.syebookstore.api.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReviewRepository extends CrudRepository<Review, Long> {

  @NativeQuery(
      "SELECT * FROM reviews "
          + "WHERE (:bookId IS NULL OR book_id = :bookId)"
          + "AND message LIKE CONCAT('%', :keyword, '%')")
  Page<Review> findByBookIdAndMessageContaining(
      @Param("bookId") Long bookId, @Param("keyword") String keyword, Pageable pageable);
}
