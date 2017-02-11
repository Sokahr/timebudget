package eu.benonline.domain.service;

import eu.benonline.domain.entity.AutomationTimeItem;
import eu.benonline.domain.entity.AutomationTimeItemRepository;
import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.entity.TimeBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Benjamin Peter.
 */
@Service
@RequiredArgsConstructor
public class TimeBudgetAutomationTimeItemServiceImpl implements TimeBudgetAutomationTimeItemService {

    private final TimeBudgetRepository timeBudgetRepository;

    private final AutomationTimeItemRepository automationTimeItemRepository;

    @Override
    public AutomationTimeItem addNewAutomationTimeItemToBudgetWithId(AutomationTimeItem automationTimeItem, long timeBudgetId) {
        TimeBudget timeBudget = timeBudgetRepository.findOne(timeBudgetId);

        if (timeBudget.addAutomationTime(automationTimeItem)) {
            automationTimeItem = automationTimeItemRepository.save(automationTimeItem);
        }
        return automationTimeItem;
    }

    @Override
    public Page<AutomationTimeItem> getAutomationTimeItemsForBudgetWithId(long timeBudgetId, Pageable pageable) {
        return automationTimeItemRepository.findByParentBudgetId(timeBudgetId, pageable);
    }

    void updateAutomationTimeItem(AutomationTimeItem automationTimeItem) {
        AutomationTimeItem persistedAutomationTimeItem = automationTimeItemRepository.findOne(automationTimeItem.getId());
        persistedAutomationTimeItem.setInterval(automationTimeItem.getInterval());
        persistedAutomationTimeItem.setHours(automationTimeItem.getHours());
        persistedAutomationTimeItem.setOpenDateRange(automationTimeItem.getOpenDateRange());
    }

    @Override
    public void removeAutomationTimeItemWithId(long automationTimeItemId) {
        AutomationTimeItem entity = automationTimeItemRepository.findOne(automationTimeItemId);
        TimeBudget parentTimeBudget = entity.getParentBudget();
        entity.getParentBudget().removeAutomationTime(entity);
        timeBudgetRepository.save(parentTimeBudget);
        automationTimeItemRepository.delete(automationTimeItemId);
    }
}
