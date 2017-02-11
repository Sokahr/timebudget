package eu.benonline.domain.service;

import eu.benonline.domain.entity.ManualTimeItem;
import eu.benonline.domain.entity.ManualTimeItemRepository;
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
public class TimeBudgetManualTimeItemServiceImpl implements TimeBudgetManualTimeItemService {
    private final TimeBudgetRepository timeBudgetRepository;
    private final ManualTimeItemRepository manualTimeItemRepository;

    @Override
    public ManualTimeItem addManualTimeItemToBudgetWithID(ManualTimeItem manualTimeItem, long timeBudgetId) {
        TimeBudget timeBudget = timeBudgetRepository.findOne(timeBudgetId);
        if (timeBudget.addManualTime(manualTimeItem)) {
            manualTimeItem = manualTimeItemRepository.save(manualTimeItem);
            timeBudgetRepository.save(timeBudget);
        }

        return manualTimeItem;
    }

    @Override
    public Page<ManualTimeItem> getManualTimeItemsForBudgetWithId(long timeBudgetId, Pageable pageable) {

        return manualTimeItemRepository.findByParentBudgetId(timeBudgetId, pageable);
    }

    @Override
    public void removeManualTimeItemWithId(long manualTimeItemId) {
        ManualTimeItem manualTimeItem = manualTimeItemRepository.findOne(manualTimeItemId);
        manualTimeItem.getParentBudget().removeManualTime(manualTimeItem);
        manualTimeItemRepository.delete(manualTimeItemId);
    }
}
