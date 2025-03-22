package io.syebookstore.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Pageables {

  public static Pageable toPageable(Integer offset, Integer limit, OrderBy orderBy) {
    if (offset == null) {
      offset = 0;
    }

    if (limit == null) {
      limit = 50;
    }

    if (orderBy == null) {
      orderBy = new OrderBy().field("id").direction(Direction.ASC);
    } else {
      if (orderBy.field() == null) {
        orderBy.field("id");
      } else {
        if (orderBy.field().equals("createdAt")) {
          orderBy.field("created_at");
        } else if (orderBy.field().equals("updatedAt")) {
          orderBy.field("updated_at");
        } else if (orderBy.field().equals("publicationDate")) {
          orderBy.field("publication_date");
        } else if (orderBy.field().equals("bookId")) {
          orderBy.field("book_id");
        } else if (orderBy.field().equals("accountId")) {
          orderBy.field("account_id");
        }
      }
      if (orderBy.direction() == null) {
        orderBy.direction(Direction.ASC);
      }
    }

    final var direction =
        orderBy.direction() == Direction.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;

    return new OffsetPageable(offset, limit, Sort.by(direction, orderBy.field()));
  }
}
