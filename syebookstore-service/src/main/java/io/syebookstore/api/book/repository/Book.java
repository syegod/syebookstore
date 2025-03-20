package io.syebookstore.api.book.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.StringJoiner;

@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String isbn;
  private String title;
  private String description;

  @Column(name = "publication_date")
  private Integer publicationDate;

  private byte[] content;
  private String[] authors;
  private String[] tags;

  public Long id() {
    return id;
  }

  public Book id(Long id) {
    this.id = id;
    return this;
  }

  public String isbn() {
    return isbn;
  }

  public Book isbn(String isbn) {
    this.isbn = isbn;
    return this;
  }

  public String title() {
    return title;
  }

  public Book title(String title) {
    this.title = title;
    return this;
  }

  public String description() {
    return description;
  }

  public Book description(String description) {
    this.description = description;
    return this;
  }

  public Integer publicationDate() {
    return publicationDate;
  }

  public Book publicationDate(Integer publicationDate) {
    this.publicationDate = publicationDate;
    return this;
  }

  public byte[] content() {
    return content;
  }

  public Book content(byte[] content) {
    this.content = content;
    return this;
  }

  public String[] authors() {
    return authors;
  }

  public Book authors(String[] authors) {
    this.authors = authors;
    return this;
  }

  public String[] tags() {
    return tags;
  }

  public Book tags(String[] tags) {
    this.tags = tags;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Book.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("isbn='" + isbn + "'")
        .add("title='" + title + "'")
        .add("description='" + description + "'")
        .add("publicationDate=" + publicationDate)
        .add("content=" + Arrays.toString(content))
        .add("authors=" + Arrays.toString(authors))
        .add("tags=" + Arrays.toString(tags))
        .toString();
  }
}
