package io.syebookstore.api.book;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class UploadBookRequest {

  private String isbn;
  private String title;
  private String description;
  private Integer publicationDate;
  private Byte[] content;
  private List<String> authors;
  private List<String> tags;

  public String isbn() {
    return isbn;
  }

  public UploadBookRequest isbn(String isbn) {
    this.isbn = isbn;
    return this;
  }

  public String title() {
    return title;
  }

  public UploadBookRequest title(String title) {
    this.title = title;
    return this;
  }

  public String description() {
    return description;
  }

  public UploadBookRequest description(String description) {
    this.description = description;
    return this;
  }

  public Integer publicationDate() {
    return publicationDate;
  }

  public UploadBookRequest publicationDate(Integer publicationDate) {
    this.publicationDate = publicationDate;
    return this;
  }

  public Byte[] content() {
    return content;
  }

  public UploadBookRequest content(Byte[] content) {
    this.content = content;
    return this;
  }

  public List<String> authors() {
    return authors;
  }

  public UploadBookRequest authors(List<String> authors) {
    this.authors = authors;
    return this;
  }

  public List<String> tags() {
    return tags;
  }

  public UploadBookRequest tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", UploadBookRequest.class.getSimpleName() + "[", "]")
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
