package eu.benonline.domain.service;

import eu.benonline.domain.entity.TimeBudget;
import org.springframework.stereotype.Service;

/**
 * Service-Layer class to manage the TimeBudgetEntities.
 * Create, receive, update, and delete TimeBudgetEntities.
 * Created by Benjamin Peter.
 */
@Service
public interface TimeBudgetService {

    TimeBudget createNewTimeBudgetWithName(String name);

    TimeBudget updateTimeBudgetNameForBudgetWithId(String timeBudgetName, long id);

    void deleteBudgetWithId(long id);

}
