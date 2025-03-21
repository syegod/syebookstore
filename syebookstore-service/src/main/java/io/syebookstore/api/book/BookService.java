package io.syebookstore.api.book;

import static io.syebookstore.api.Pageables.toPageable;

import io.syebookstore.api.book.repository.Book;
import io.syebookstore.api.book.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Book uploadBook(UploadBookRequest request) {
    return bookRepository.save(
        new Book()
            .isbn(request.isbn())
            .title(request.title())
            .description(request.description())
            .publicationDate(request.publicationDate())
            .content(request.content())
            .authors(request.authors().toArray(String[]::new))
            .tags(request.tags().toArray(String[]::new)));
  }

  public Page<Book> listBooks(ListBooksRequest request) {
    final var pageable = toPageable(request.offset(), request.limit(), request.orderBy());

    final var k = request.keyword() != null ? request.keyword() : "";
    return bookRepository.findBooks(k, request.tags(), pageable);
  }
}
