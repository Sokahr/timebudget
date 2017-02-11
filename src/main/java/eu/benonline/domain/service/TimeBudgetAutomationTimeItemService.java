package eu.benonline.domain.service;

import eu.benonline.domain.entity.AutomationTimeItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service-Layer to handle create, retrieve, update, and delete AutomationTimeItems from a TimeBudget.
 * <p>
 * Created by Benjamin Peter.
 */
@Service
public interface TimeBudgetAutomationTimeItemService {

    AutomationTimeItem addNewAutomationTimeItemToBudgetWithId(AutomationTimeItem automationTimeItem, long timeBudgetId);

    Page<AutomationTimeItem> getAutomationTimeItemsForBudgetWithId(long timeBudgetId, Pageable pageable);

    void removeAutomationTimeItemWithId(long automationTimeItemId);
}
