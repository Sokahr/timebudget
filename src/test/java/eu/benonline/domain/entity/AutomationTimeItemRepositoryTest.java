package eu.benonline.domain.entity;

import eu.benonline.domain.vo.AutomationInterval;
import eu.benonline.domain.vo.OpenDateRange;
import eu.benonline.domain.vo.WorkingHours;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test to test the basic repository functions.
 * Created by Benjamin Peter.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AutomationTimeItemRepositoryTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TestEntityManager entityManager;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private AutomationTimeItemRepository automationTimeItemRepository;
    private TimeBudget parentTimeBudget;
    private AutomationTimeItem beginsBefore2017_12_31;
    private AutomationTimeItem endsBefore2017_12_31;
    private AutomationTimeItem beginsAfter2017_12_31;

    @Before
    public void setUp() throws Exception {

        parentTimeBudget = new TimeBudget("parent");

        beginsBefore2017_12_31 = new AutomationTimeItem(new WorkingHours(5), AutomationInterval
                .DAILY, new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        endsBefore2017_12_31 = new AutomationTimeItem(new WorkingHours(40), AutomationInterval
                .ONCE, new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 5)));
        beginsAfter2017_12_31 = new AutomationTimeItem(new WorkingHours(160), AutomationInterval
                .MONTHLY, new OpenDateRange(LocalDate.of(2018, 1, 1), null));

        parentTimeBudget.addAutomationTime(beginsBefore2017_12_31);
        parentTimeBudget.addAutomationTime(endsBefore2017_12_31);
        parentTimeBudget.addAutomationTime(beginsAfter2017_12_31);

        entityManager.persist(parentTimeBudget);
    }

    @Test
    public void findByParentBudgetId() throws Exception {
        assertThat(automationTimeItemRepository.findByParentBudgetId(parentTimeBudget.getId(), null))
                .containsExactlyInAnyOrder(beginsAfter2017_12_31, beginsBefore2017_12_31, endsBefore2017_12_31);
    }
}