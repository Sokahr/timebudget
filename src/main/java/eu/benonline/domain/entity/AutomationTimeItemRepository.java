package eu.benonline.domain.entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Small footprint repository for AutomationTimeItems
 * Created by Benjamin Peter.
 */
public interface AutomationTimeItemRepository extends Repository<AutomationTimeItem, Long> {

    AutomationTimeItem save(AutomationTimeItem item);

    AutomationTimeItem findOne(long automationTimeItemId);

    void delete(long automationTimeItemId);

    Page<AutomationTimeItem> findByParentBudgetId(long parentBudgetId, Pageable pageable);

}
