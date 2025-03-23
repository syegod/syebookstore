package io.syebookstore.api.book;

import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import java.util.List;
import java.util.stream.IntStream;

public class BookVocabulary {

  private BookVocabulary() {}

  public static String title() {
    return randomAlphanumeric(10);
  }

  public static String isbn() {
    return randomAlphanumeric(13);
  }

  public static String description() {
    return randomAlphanumeric(10);
  }

  public static byte[] content() {
    return new byte[1000];
  }

  public static int publicationDate() {
    return 2000;
  }

  public static List<String> authors() {
    return List.of(randomAlphanumeric(15), randomAlphanumeric(15));
  }

  public static List<String> tags() {
    return IntStream.range(0, 5).mapToObj(e -> randomAlphanumeric(7)).toList();
  }
}
