package io.syebookstore.api.book;

import static io.syebookstore.api.book.BookMappers.toBookInfo;

import io.syebookstore.annotations.Protected;
import io.syebookstore.api.ServiceException;
import io.syebookstore.api.book.repository.Book;
import java.time.LocalDate;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/book")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping("/uploadBook")
  @Protected
  public BookInfo uploadBook(@RequestBody UploadBookRequest request) {
    final var title = request.title();
    if (title == null || (title.length() < 8 || title.length() > 64)) {
      throw new ServiceException(400, "Missing or invalid: title");
    }

    final var isbn = request.isbn();
    if (isbn == null || isbn.length() != 13) {
      throw new ServiceException(400, "Missing or invalid: isbn");
    }

    final var description = request.description();
    if (description == null || (description.length() < 8 || description.length() > 500)) {
      throw new ServiceException(400, "Missing or invalid: description");
    }

    final var publicationDate = request.publicationDate();
    if (publicationDate == null || (publicationDate > LocalDate.now().getYear())) {
      throw new ServiceException(400, "Missing or invalid: publication date");
    }

    final var content = request.content();
    if (content == null) {
      throw new ServiceException(400, "Missing or invalid: content");
    }

    final var authors = request.authors();
    if (authors == null || (authors.isEmpty() || authors.size() > 5)) {
      throw new ServiceException(400, "Missing or invalid: authors");
    }

    final var tags = request.tags();
    if (tags == null || (tags.isEmpty() || tags.size() > 5)) {
      throw new ServiceException(400, "Missing or invalid: tags");
    }

    Book book;
    try {
      book = bookService.uploadBook(request);
    } catch (DataAccessException e) {
      if (e.getMessage().contains("duplicate key value violates unique constraint")) {
        throw new ServiceException(400, "Cannot upload book: already exists");
      }
      throw e;
    }

    return toBookInfo(book);
  }

  @PostMapping("/listBooks")
  public ListBooksResponse listBooks(@RequestBody ListBooksRequest request) {
    final var keyword = request.keyword();
    if (keyword != null && (keyword.length() < 4 || keyword.length() > 64)) {
      throw new ServiceException(400, "Missing or invalid: keyword");
    }

    final var tags = request.tags();
    if ((tags != null && !tags.isEmpty()) && tags.size() > 10) {
      throw new ServiceException(400, "Missing or invalid: tags");
    }

    final var offset = request.offset();
    if (offset != null && offset < 0) {
      throw new ServiceException(400, "Missing or invalid: offset");
    }

    final var limit = request.limit();
    if (limit != null && (limit < 0 || limit > 50)) {
      throw new ServiceException(400, "Missing or invalid: limit");
    }

    final var bookPage = bookService.listBooks(request);

    final var bookInfos = bookPage.get().map(BookMappers::toBookInfo).toList();

    return new ListBooksResponse()
        .bookInfos(bookInfos)
        .limit(limit)
        .offset(offset)
        .totalCount(bookPage.getTotalElements());
  }

  @PostMapping("/getBook")
  public BookInfo getBook(@RequestBody Long id) {
    return toBookInfo(bookService.getBook(id));
  }

  @PostMapping("/downloadBook")
  @Protected
  public ResponseEntity<byte[]> downloadBook(@RequestBody Long id) {
    final var book = bookService.downloadBook(id);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", book.title() + ".pdf");

    return new ResponseEntity<>(book.content(), headers, HttpStatus.OK);
  }
}
