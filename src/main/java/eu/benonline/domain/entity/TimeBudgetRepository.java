package eu.benonline.domain.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link TimeBudget}
 * Created by Benjamin Peter on 11.12.2016.
 */
public interface TimeBudgetRepository extends CrudRepository<TimeBudget, Long> {

    Page<TimeBudget> findAll(Pageable pageable);
}
