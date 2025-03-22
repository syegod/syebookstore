package io.syebookstore.api.book;

import io.syebookstore.api.book.repository.Book;
import java.util.Arrays;

public class BookMappers {

  private BookMappers() {}

  public static BookInfo toBookInfo(Book book) {
    return new BookInfo()
        .id(book.id())
        .title(book.title())
        .isbn(book.isbn())
        .description(book.description())
        .publicationDate(book.publicationDate())
        .content(book.content())
        .authors(Arrays.asList(book.authors()))
        .tags(Arrays.asList(book.tags()));
  }
}
