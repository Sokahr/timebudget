package eu.benonline.domain.service;

import eu.benonline.domain.entity.ManualTimeItem;
import eu.benonline.domain.entity.ManualTimeItemRepository;
import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.entity.TimeBudgetRepository;
import eu.benonline.domain.vo.DateRange;
import eu.benonline.domain.vo.TimeType;
import eu.benonline.domain.vo.WorkingHours;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Benjamin Peter.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TimeBudgetManualTimeItemServiceImplTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TestEntityManager entityManager;

    private TimeBudgetManualTimeItemServiceImpl timeBudgetManualTimeItemService;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TimeBudgetRepository timeBudgetRepository;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ManualTimeItemRepository manualTimeItemRepository;

    @Before
    public void setUp() throws Exception {
        timeBudgetManualTimeItemService = new TimeBudgetManualTimeItemServiceImpl(timeBudgetRepository, manualTimeItemRepository);
    }

    @Test
    public void addManualTimeItemToBudgetWithID() throws Exception {
        ManualTimeItem manualTimeItem = new ManualTimeItem(new WorkingHours(9),
                new DateRange(LocalDate.of(2017, 1, 3), LocalDate.of(2017, 1, 3)), TimeType.DONE);
        TimeBudget timeBudget = entityManager.persistAndFlush(new TimeBudget("test"));
        ManualTimeItem createdManualTimeItem = timeBudgetManualTimeItemService.addManualTimeItemToBudgetWithID
                (manualTimeItem,
                        timeBudget.getId());

        ManualTimeItem persistedManualItem = entityManager.find(ManualTimeItem.class, createdManualTimeItem.getId());
        assertThat(persistedManualItem).as("is manual time item with id %d persisted", createdManualTimeItem.getId())
                .isEqualToComparingOnlyGivenFields(manualTimeItem, "hours", "timeType", "dateRange");

        TimeBudget persistedBudget = entityManager.find(TimeBudget.class, timeBudget.getId());
        assertThat(persistedBudget.getManualItems()).as("has the time Budget the manualTimeItem").contains(persistedManualItem);
    }

    @Test
    public void getManualTimeItemsForBudgetWithId() throws Exception {
        TimeBudget timeBudget = entityManager.persist(new TimeBudget("test"));
        long timBudgetId = timeBudget.getId();

        ManualTimeItem manualTimeItem = new ManualTimeItem(new WorkingHours(2),
                new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)), TimeType.DONE);

        timeBudget.addManualTime(manualTimeItem);
        entityManager.flush();

        assertThat(timeBudgetManualTimeItemService.getManualTimeItemsForBudgetWithId(timBudgetId,
                new PageRequest(0, 1))).contains(manualTimeItem);
    }

    @Test
    public void removeManualTimeItemWithId() throws Exception {
        TimeBudget timeBudget = new TimeBudget("test");
        ManualTimeItem timeItemToBeDeleted = new ManualTimeItem(new WorkingHours(8),
                new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)), TimeType.HOLIDAY);

        timeBudget.addManualTime(timeItemToBeDeleted);
        timeBudget = entityManager.persistAndFlush(timeBudget);

        timeBudgetManualTimeItemService.removeManualTimeItemWithId(timeItemToBeDeleted.getId());

        assertThat(entityManager.find(TimeBudget.class, timeBudget.getId()).getManualItems()).isEmpty();
        assertThat(entityManager.find(ManualTimeItem.class, timeItemToBeDeleted.getId())).isNull();

    }

}