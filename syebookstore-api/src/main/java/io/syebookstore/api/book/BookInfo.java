package io.syebookstore.api.book;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class BookInfo {
  private Long id;
  private String isbn;
  private String title;
  private String description;
  private Integer publicationDate;
  private Byte[] content;
  private List<String> authors;
  private List<String> tags;

  public Long id() {
    return id;
  }

  public BookInfo id(Long id) {
    this.id = id;
    return this;
  }

  public String isbn() {
    return isbn;
  }

  public BookInfo isbn(String isbn) {
    this.isbn = isbn;
    return this;
  }

  public String title() {
    return title;
  }

  public BookInfo title(String title) {
    this.title = title;
    return this;
  }

  public String description() {
    return description;
  }

  public BookInfo description(String description) {
    this.description = description;
    return this;
  }

  public Integer publicationDate() {
    return publicationDate;
  }

  public BookInfo publicationDate(Integer publicationDate) {
    this.publicationDate = publicationDate;
    return this;
  }

  public Byte[] content() {
    return content;
  }

  public BookInfo content(Byte[] content) {
    this.content = content;
    return this;
  }

  public List<String> authors() {
    return authors;
  }

  public BookInfo authors(List<String> authors) {
    this.authors = authors;
    return this;
  }

  public List<String> tags() {
    return tags;
  }

  public BookInfo tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BookInfo.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("isbn='" + isbn + "'")
        .add("title='" + title + "'")
        .add("description='" + description + "'")
        .add("publicationDate=" + publicationDate)
        .add("content=" + Arrays.toString(content))
        .add("authors=" + authors)
        .add("tags=" + tags)
        .toString();
  }
}
