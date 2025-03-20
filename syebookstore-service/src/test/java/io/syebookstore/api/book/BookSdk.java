package io.syebookstore.api.book;

public interface BookSdk {

  Long uploadBook(UploadBookRequest request);

  ListBooksResponse listBooks(ListBooksRequest request);

  BookInfo getBook(Long id);

  int[] downloadBook(Long id);
}
