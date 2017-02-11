package eu.benonline.domain.service;

import eu.benonline.domain.entity.ManualTimeItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service layer class to Create, receive, update, and delete ManualTimeItems from a TimeBudget.
 * Created by Benjamin Peter.
 */
public interface TimeBudgetManualTimeItemService {

    ManualTimeItem addManualTimeItemToBudgetWithID(ManualTimeItem manualTimeItem, long timeBudgetId);

    Page<ManualTimeItem> getManualTimeItemsForBudgetWithId(long timeBudgetId, Pageable pageable);

    void removeManualTimeItemWithId(long manualTimeItemId);

}
