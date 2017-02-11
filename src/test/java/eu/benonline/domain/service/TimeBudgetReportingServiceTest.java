package eu.benonline.domain.service;

import eu.benonline.domain.entity.AutomationTimeItem;
import eu.benonline.domain.entity.ManualTimeItem;
import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.entity.TimeBudgetRepository;
import eu.benonline.domain.vo.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * Created by Benjamin Peter.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TimeBudgetReportingServiceTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TestEntityManager entityManager;

    private TimeBudgetReportingServiceImpl timeBudgetReportingService;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private TimeBudgetRepository timeBudgetRepository;

    @Before
    public void setUp() throws Exception {

        timeBudgetReportingService = new TimeBudgetReportingServiceImpl(timeBudgetRepository);

        TimeBudget positiveBalance = new TimeBudget("positiveBalance");

        AutomationTimeItem sevenDaysTwoHoursWork = new AutomationTimeItem(new WorkingHours(2),
                AutomationInterval.DAILY,
                new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 6)));
        positiveBalance.addAutomationTime(sevenDaysTwoHoursWork);
        //each day an hour extra:
        for (int i = 0; i < 6; i++) {
            LocalDate day = LocalDate.of(2017, 1, 1);
            day = day.plusDays(i);

            ManualTimeItem threeHoursDone = new ManualTimeItem(new WorkingHours(3),
                    new DateRange(day, day), TimeType.DONE);
            positiveBalance.addManualTime(threeHoursDone);
        }

        TimeBudget balanced = new TimeBudget("balanced");

        AutomationInterval[] workdays = new AutomationInterval[]{AutomationInterval.MONDAYS,
                AutomationInterval.TUESDAYS,
                AutomationInterval.WEDNESDAYS,
                AutomationInterval.THURSDAYS,
                AutomationInterval.FRIDAYS};

        for (AutomationInterval interval : workdays
                ) {
            AutomationTimeItem work = new AutomationTimeItem(new WorkingHours(8), interval,
                    new OpenDateRange(LocalDate.of(2017, 1, 2), null));
            balanced.addAutomationTime(work);
        }
        //balanced till 2017/01/29
        for (int i = 0; i < 28; i++) {
            LocalDate day = LocalDate.of(2017, 1, 2);
            day = day.plusDays(i);
            if (day.getDayOfWeek().get(ChronoField.DAY_OF_WEEK) < 6) {
                TimeType timeType = TimeType.values()[i % TimeType.values().length];
                ManualTimeItem manualTimeItem = new ManualTimeItem(new WorkingHours(8), new DateRange(day, day),
                        timeType);
                balanced.addManualTime(manualTimeItem);
            }
        }

        TimeBudget negativeBalance = new TimeBudget("negativeBalance");
        AutomationTimeItem onceNeverDid = new AutomationTimeItem(new WorkingHours(40), AutomationInterval.ONCE,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        negativeBalance.addAutomationTime(onceNeverDid);

        entityManager.persistAndFlush(positiveBalance);
        entityManager.persistAndFlush(balanced);
        entityManager.persistAndFlush(negativeBalance);
    }

    @Test
    public void getBalanceOfAllTimeBudgetsTillDate() throws Exception {
        Page<TimeBudgetReport> reports = timeBudgetReportingService.getBalanceTill(LocalDate.of(2017, 1, 29),
                new PageRequest(0, 3));

        assertThat(reports)
                .hasSize(3)
                .extracting("budgetName", "balance")
                .contains(tuple("positiveBalance", 6f),
                        tuple("balanced", 0f),
                        tuple("negativeBalance", -40f));

        Page<TimeBudgetReport> reportOneOnOne = timeBudgetReportingService.getBalanceTill(LocalDate.of(2017, 1, 29),
                new PageRequest(0, 1));

        assertThat(reportOneOnOne.getTotalPages()).as("3 Pages expected.").isEqualTo(3);

        Page<TimeBudgetReport> reportWithoutPaging = timeBudgetReportingService.getBalanceTill(
                LocalDate.of(2017, 1, 29), null);
        assertThat(reportWithoutPaging).as("Without paging the only Page returned contains all.").hasSize(3);
    }
}