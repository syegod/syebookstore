package io.syebookstore;

import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import java.lang.reflect.Field;
import java.util.Arrays;

public class AssertionUtils {
  public static Field[] getFields(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields()).toArray(Field[]::new);
  }

  public static String ra(int charCount) {
    return randomAlphanumeric(charCount);
  }

  public static String ra(int charFrom, int charToExc) {
    return randomAlphanumeric(charFrom, charToExc);
  }
}
