package eu.benonline.domain.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Small footprint repository for ManualTimeItems
 * Created by Benjamin Peter.
 */
@org.springframework.stereotype.Repository
public interface ManualTimeItemRepository extends Repository<ManualTimeItem, Long> {
    ManualTimeItem findOne(long id);

    void delete(long id);

    Page<ManualTimeItem> findByParentBudgetId(long parentBudgetId, Pageable pageable);

    ManualTimeItem save(ManualTimeItem item);
}
