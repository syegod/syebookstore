package io.syebookstore.api.book;

import static io.syebookstore.api.Pageables.toPageable;

import io.syebookstore.api.ServiceException;
import io.syebookstore.api.book.repository.Book;
import io.syebookstore.api.book.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public Book uploadBook(UploadBookRequest request) {
    final var book =
        new Book()
            .isbn(request.isbn())
            .title(request.title())
            .description(request.description())
            .publicationDate(request.publicationDate())
            .content(request.content())
            .authors(request.authors().toArray(String[]::new))
            .tags(request.tags().toArray(String[]::new));

    LOGGER.info("Saving book: {}", book.title());

    return bookRepository.save(book);
  }

  @Transactional(readOnly = true)
  public Page<Book> listBooks(ListBooksRequest request) {
    final var pageable = toPageable(request.offset(), request.limit(), request.orderBy());

    final var k = request.keyword() != null ? request.keyword() : "";
    final var bookPage = bookRepository.findBooks(k, request.tags(), pageable);

    LOGGER.info("Getting book list with total {} elements", bookPage.getTotalElements());

    return bookPage;
  }

  @Transactional(readOnly = true)
  public Book getBook(Long id) {
    final var book = bookRepository.findById(id).orElse(null);
    if (book == null) {
      throw new ServiceException(404, "Book not found");
    }

    LOGGER.info("Getting book: {}", book.title());

    return book;
  }

}
