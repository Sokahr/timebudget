package eu.benonline.domain.service;

import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.entity.TimeBudgetRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the {@link TimeBudgetServiceImpl}
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class TimeTimeBudgetServiceImplTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TestEntityManager entityManager;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TimeBudgetRepository timeBudgetRepository;

    private TimeBudgetServiceImpl budgetService;

    @Before
    public void setup() {
        budgetService = new TimeBudgetServiceImpl(timeBudgetRepository);

    }

    @Test
    public void createNewTimeBudgetWithName_actuallyPersistsANewTimeBudgetEntityWithThatName() throws
            Exception {

        TimeBudget newTimeBudgetWithName = this.budgetService.createNewTimeBudgetWithName("New Time Budget");
        assertThat(entityManager.find(TimeBudget.class, newTimeBudgetWithName.getId()))
                .isEqualToComparingFieldByField(newTimeBudgetWithName)
                .extracting("name").containsExactly("New Time Budget");

    }

    @Test
    public void getTimeBudgetWithId_retrieveTheCorrectEntity() throws Exception {
        assertThat(this.budgetService.getTimeBudgetWithId(1)).as("No entity with id 1").isNull();
        TimeBudget persistedBudget = this.entityManager.persistFlushFind(new TimeBudget("persisted time"));
        assertThat(this.budgetService.getTimeBudgetWithId(persistedBudget.getId()))
                .as("find right now persisted entity")
                .isEqualTo(persistedBudget);
    }

    @Test
    public void getAllTimeBudgets_retrievesAllPersistedEntities() throws Exception {
        assertThat(this.budgetService.getAllTimeBudgets()).as("No entities persisted").isNullOrEmpty();
        this.entityManager.persist(new TimeBudget("One"));
        this.entityManager.persist(new TimeBudget("two"));
        assertThat(this.budgetService.getAllTimeBudgets())
                .as("two time budget entities persisted")
                .hasSize(2)
                .extracting("name").contains("One", "two");
    }

    @Test
    public void updateTimeBudgetNameForBudgetWithId_changePersistedTimeBudgetAndReturnsIt() throws Exception {
        assertThat(this.budgetService.updateTimeBudgetNameForBudgetWithId("no time budget", 1))
                .isNull();
        long persistedId = (long) this.entityManager.persistAndGetId(new TimeBudget("old name"));
        assertThat(this.budgetService.updateTimeBudgetNameForBudgetWithId("new name", persistedId))
                .isNotNull().extracting("name", "id").containsExactly("new name", persistedId).doesNotContain("old name");
        assertThat(this.entityManager.find(TimeBudget.class, persistedId)).isNotNull().hasFieldOrPropertyWithValue
                ("name", "new name");
    }

    @Test
    public void deleteBudgetWithId_deletesTheEntity() throws Exception {
        long persistedId = (long) this.entityManager.persistAndGetId(new TimeBudget("I have to be deleted"));
        this.budgetService.deleteBudgetWithId(persistedId);
        assertThat(this.entityManager.find(TimeBudget.class, persistedId)).as("entityManager could not found the " +
                "TimeBudget with the Id $d anymore", persistedId).isNull();
    }
}