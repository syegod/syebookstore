package io.syebookstore.api.book;

import static io.syebookstore.api.Pageables.toPageable;

import io.syebookstore.api.ServiceException;
import io.syebookstore.api.book.repository.Book;
import io.syebookstore.api.book.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional(readOnly = true)
  public Page<Book> listBooks(ListBooksRequest request) {
    final var pageable = toPageable(request.offset(), request.limit(), request.orderBy());

    final var k = request.keyword() != null ? request.keyword() : "";
    return bookRepository.findBooks(k, request.tags(), pageable);
  }

  @Transactional(readOnly = true)
  public Book getBook(Long id) {
    final var book = bookRepository.findById(id).orElse(null);
    if (book == null) {
      throw new ServiceException(404, "Book not found");
    }

    return book;
  }

  @Transactional(readOnly = true)
  public Book downloadBook(Long id) {
    final var book = bookRepository.findById(id).orElse(null);
    if (book == null) {
      throw new ServiceException(404, "Book not found");
    }

    return book;
  }
}
