package eu.benonline.domain.service;

import eu.benonline.domain.entity.AutomationTimeItem;
import eu.benonline.domain.entity.AutomationTimeItemRepository;
import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.entity.TimeBudgetRepository;
import eu.benonline.domain.vo.AutomationInterval;
import eu.benonline.domain.vo.OpenDateRange;
import eu.benonline.domain.vo.WorkingHours;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Benjamin Peter.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TimeBudgetAutomationTimeItemServiceImplTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TestEntityManager entityManager;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TimeBudgetRepository timeBudgetRepository;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private AutomationTimeItemRepository automationTimeItemRepository;

    private TimeBudgetAutomationTimeItemServiceImpl timeBudgetAutomationTimeItemService;

    @Before
    public void setUp() throws Exception {
        timeBudgetAutomationTimeItemService = new TimeBudgetAutomationTimeItemServiceImpl(timeBudgetRepository, automationTimeItemRepository);
    }

    @Test
    public void addNewAutomationTimeItemToBudgetWithId() throws Exception {
        TimeBudget timeBudget = entityManager.persistAndFlush(new TimeBudget("AutomationItem Service test"));
        AutomationTimeItem automationTimeItem = new AutomationTimeItem(new WorkingHours(8), AutomationInterval
                .DAILY, new OpenDateRange(LocalDate.of(2017, 1, 1), null));

        AutomationTimeItem createdAutomationTimeItem = timeBudgetAutomationTimeItemService.addNewAutomationTimeItemToBudgetWithId(automationTimeItem, timeBudget
                .getId());
        assertThat(entityManager.find(AutomationTimeItem.class, createdAutomationTimeItem.getId())).isNotNull();
        assertThat(createdAutomationTimeItem).extracting("parentBudget.id").contains(timeBudget.getId());

        assertThat(this.entityManager.find(TimeBudget.class, timeBudget.getId()).getAutomationTimes())
                .hasSize(1)
                .first().isEqualTo(createdAutomationTimeItem);
    }

    @Test
    public void getAutomationTimeItemsForBudgetWithId() throws Exception {
        TimeBudget timeBudget = entityManager.persistAndFlush(new TimeBudget("test"));
        AutomationTimeItem itemMo = new AutomationTimeItem(new WorkingHours(8), AutomationInterval.MONDAYS,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        AutomationTimeItem itemTu = new AutomationTimeItem(new WorkingHours(8), AutomationInterval.TUESDAYS,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        AutomationTimeItem itemWe = new AutomationTimeItem(new WorkingHours(8), AutomationInterval.WEDNESDAYS,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        AutomationTimeItem itemTh = new AutomationTimeItem(new WorkingHours(8), AutomationInterval.THURSDAYS,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        AutomationTimeItem itemFr = new AutomationTimeItem(new WorkingHours(8), AutomationInterval.FRIDAYS,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        timeBudget.addAutomationTime(itemMo);
        timeBudget.addAutomationTime(itemTu);
        timeBudget.addAutomationTime(itemWe);
        timeBudget.addAutomationTime(itemTh);
        timeBudget.addAutomationTime(itemFr);
        timeBudget = entityManager.persistAndFlush(timeBudget);

        Page<AutomationTimeItem> automationTimeItemPageAll = timeBudgetAutomationTimeItemService
                .getAutomationTimeItemsForBudgetWithId(timeBudget.getId(), null);

        assertThat(automationTimeItemPageAll).hasSize(5).contains(itemMo, itemTu, itemWe, itemTh, itemFr);
    }

    @Test
    public void updateAutomationTimeItem() throws Exception {
        AutomationTimeItem automationTimeItem = new AutomationTimeItem(new WorkingHours(2), AutomationInterval.ONCE,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        automationTimeItem = entityManager.persistAndFlush(automationTimeItem);

        AutomationTimeItem updateCopy = new AutomationTimeItem(new WorkingHours(4), AutomationInterval.MONTHLY,
                new OpenDateRange(LocalDate.of(2017, 2, 1), null));
        updateCopy.setId(automationTimeItem.getId());

        timeBudgetAutomationTimeItemService.updateAutomationTimeItem(updateCopy);

        assertThat(this.entityManager.find(AutomationTimeItem.class, automationTimeItem.getId()))
                .isEqualToComparingOnlyGivenFields(updateCopy, "hours", "interval", "openDateRange");
    }

    @Test
    public void removeAutomationTimeItemWithId() throws Exception {
        TimeBudget timeBudget = new TimeBudget("test");
        AutomationTimeItem automationTimeItem = new AutomationTimeItem(new WorkingHours(7), AutomationInterval.DAILY,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        timeBudget.addAutomationTime(automationTimeItem);
        timeBudget = entityManager.persistAndFlush(timeBudget);

        timeBudgetAutomationTimeItemService.removeAutomationTimeItemWithId(automationTimeItem.getId());

        assertThat(entityManager.find(AutomationTimeItem.class, automationTimeItem.getId())).isNull();
        assertThat(entityManager.find(TimeBudget.class, timeBudget.getId()).getAutomationTimes()).isEmpty();
    }
}