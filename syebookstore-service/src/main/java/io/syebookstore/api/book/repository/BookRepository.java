package io.syebookstore.api.book.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface BookRepository extends CrudRepository<Book, Long> {

  @NativeQuery(
      "SELECT * FROM books "
          + "WHERE (title LIKE CONCAT('%', :keyword, '%') "
          + "OR description LIKE CONCAT('%', :keyword, '%'))")
  Page<Book> findBooks(@Param("keyword") String keyword, Pageable pageable);

  @NativeQuery(
      "SELECT * FROM books "
          + "WHERE (title LIKE CONCAT('%', :keyword, '%') "
          + "OR description LIKE CONCAT('%', :keyword, '%')) "
          + "AND tags && :tags")
  Page<Book> findBooksWithTags(
      @Param("keyword") String keyword, @Param("tags") String[] tags, Pageable pageable);
}
