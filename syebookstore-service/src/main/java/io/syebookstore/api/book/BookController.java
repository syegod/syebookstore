package io.syebookstore.api.book;

import io.syebookstore.annotations.Protected;
import io.syebookstore.api.ServiceException;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/book")
public class BookController {

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
    if (publicationDate == null || (publicationDate > LocalDate.now().getYear() + 2)) {
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

    return new BookInfo();
  }
}
