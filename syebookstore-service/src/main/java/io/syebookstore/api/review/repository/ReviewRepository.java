package io.syebookstore.api.review.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReviewRepository extends CrudRepository<Review, Long> {

}
