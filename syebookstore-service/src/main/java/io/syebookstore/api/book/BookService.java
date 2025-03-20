package io.syebookstore.api.book;

import io.syebookstore.api.book.repository.Book;
import io.syebookstore.api.book.repository.BookRepository;
import jakarta.transaction.Transactional;
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
}
