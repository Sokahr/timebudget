package eu.benonline.domain.service;

import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.entity.TimeBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by Benjamin Peter.
 */
@Service
@RequiredArgsConstructor
public class TimeBudgetServiceImpl implements TimeBudgetService {

    private final TimeBudgetRepository timeBudgetRepository;

    @Override
    public TimeBudget createNewTimeBudgetWithName(String name) {
        return timeBudgetRepository.save(new TimeBudget(name));
    }

    TimeBudget getTimeBudgetWithId(long id) {
        return timeBudgetRepository.findOne(id);
    }

    Iterable<TimeBudget> getAllTimeBudgets() {
        return timeBudgetRepository.findAll();
    }

    @Override
    public TimeBudget updateTimeBudgetNameForBudgetWithId(String timeBudgetName, long id) {
        TimeBudget timeBudget = timeBudgetRepository.findOne(id);
        if (timeBudget != null) {
            timeBudget.setName(timeBudgetName);
        }
        return timeBudget;
    }

    @Override
    public void deleteBudgetWithId(long id) {
        timeBudgetRepository.delete(id);
    }
}
