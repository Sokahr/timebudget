package eu.benonline.domain.entity;

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
 * Integration Test to test the basic repository functions.
 * Created by Benjamin Peter.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ManualTimeItemRepositoryTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TestEntityManager entityManager;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ManualTimeItemRepository manualTimeItemRepository;

    private TimeBudget budget;
    private ManualTimeItem manualTimeItem;

    @Before
    public void setUp() throws Exception {

        budget = entityManager.persist(new TimeBudget("Test"));
        manualTimeItem = entityManager.persist(new ManualTimeItem(new WorkingHours(2), new DateRange
                (LocalDate.of(2017, 1, 1), LocalDate.of(2018, 1, 1)), TimeType.DONE));
        budget.addManualTime(manualTimeItem);
        budget = entityManager.persist(budget);
    }

    @Test
    public void findOne() throws Exception {
        assertThat(this.manualTimeItemRepository.findOne(manualTimeItem.getId())).isEqualToComparingFieldByField
                (manualTimeItem);
        assertThat(this.manualTimeItemRepository.findOne(0)).isNull();
    }

    @Test
    public void findByParentBudgetId() throws Exception {
        assertThat(this.manualTimeItemRepository.findByParentBudgetId(budget.getId(), new PageRequest(0, 1))).contains
                (manualTimeItem);
        assertThat(this.manualTimeItemRepository.findByParentBudgetId(0, null)).isNullOrEmpty();
    }

}