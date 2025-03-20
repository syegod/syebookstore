package io.syebookstore.api.book.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public interface BookRepository extends CrudRepository<Book, Long> {}
