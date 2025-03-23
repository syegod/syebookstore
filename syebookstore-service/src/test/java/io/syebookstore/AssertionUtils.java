package io.syebookstore;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.syebookstore.api.Direction;
import io.syebookstore.api.OrderBy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiConsumer;

public class AssertionUtils {

  private AssertionUtils() {}

  public static Field[] getFields(Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields()).toArray(Field[]::new);
  }

  public static <E> void assertCollections(
      Collection<E> expected, Collection<E> actual, BiConsumer<E, E> consumer) {
    if (expected == null) {
      assertNull(actual);
      return;
    }

    assertNotNull(actual);
    assertEquals(expected.size(), actual.size(), "size");

    final var expectedList = new ArrayList<>(expected);
    final var actualList = new ArrayList<>(actual);

    for (int i = 0; i < expectedList.size(); i++) {
      consumer.accept(expectedList.get(i), actualList.get(i));
    }
  }

  public static <T> Comparator<? super T> toComparator(OrderBy orderBy) {
    final var field = orderBy.field() != null ? orderBy.field() : "id";
    final var direction = orderBy.direction() != null ? orderBy.direction() : Direction.ASC;
    final var comparator = Comparator.comparing(t -> getFieldValue(field, t));
    return direction == Direction.ASC ? comparator : comparator.reversed();
  }

  public static <T, V> V getFieldValue(String name, T obj) {
    try {
      final Class<?> clazz = obj.getClass();
      final var field = clazz.getDeclaredField(name);
      field.setAccessible(true);
      //noinspection unchecked
      return (V) field.get(obj);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
