package io.syebookstore.api.book.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Transactional
public interface BookRepository extends CrudRepository<Book, Long> {

  // TODO: https://github.com/syegod/syebookstore/issues/7
  @NativeQuery(
      "SELECT * FROM books "
          + "WHERE (title LIKE CONCAT('%', :keyword, '%') "
          + "OR description LIKE CONCAT('%', :keyword, '%'))")
  Page<Book> findBooks(
      @Param("keyword") String keyword, @Param("tags") List<String> tags, Pageable pageable);
}
