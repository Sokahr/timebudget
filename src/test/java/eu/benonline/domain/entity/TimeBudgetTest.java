package eu.benonline.domain.entity;

import eu.benonline.domain.vo.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Benjamin Peter.
 */
public class TimeBudgetTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_RejectsNullAsName() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("name");
        new TimeBudget(null);
    }

    @Test
    public void constructor_RejectsEmptyStringAsName() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Name cannot be empty");

        new TimeBudget("");
    }

    @Test
    public void setName_RejectsNull() throws Exception {
        TimeBudget budget = new TimeBudget("test");

        thrown.expect(NullPointerException.class);
        thrown.expectMessage("name");

        budget.setName(null);
    }

    @Test
    public void setName_RejectsEmptyString() throws Exception {
        TimeBudget budget = new TimeBudget("test");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Name cannot be empty");

        budget.setName("");
    }

    @Test
    public void addAutomationTime_RejectsNullForAutomationTimeItem() throws Exception {
        TimeBudget budget = new TimeBudget("test");

        thrown.expect(NullPointerException.class);
        thrown.expectMessage("automationTimeItem");

        budget.addAutomationTime(null);
    }

    @Test
    public void addAutomationTime_setTheParentBudgetToTheTimeBudget() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        AutomationTimeItem automationTimeItem = new AutomationTimeItem(new WorkingHours(1), AutomationInterval
                .SUNDAYS, new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        budget.addAutomationTime(automationTimeItem);
        assertThat(automationTimeItem.getParentBudget()).isEqualTo(budget);

    }

    @Test
    public void getAutomationTimes_ReturnsIterableListOfCreatedAutomationTimeItems() throws Exception {
        TimeBudget budget = new TimeBudget("test");

        AutomationTimeItem firstItem = new AutomationTimeItem(new WorkingHours(2),
                AutomationInterval.DAILY,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        budget.addAutomationTime(firstItem);
        AutomationTimeItem secondItem = new AutomationTimeItem(new WorkingHours(4),
                AutomationInterval.FRIDAYS,
                new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2018, 1, 1)));
        budget.addAutomationTime(secondItem);

        assertThat(budget.getAutomationTimes())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(firstItem, secondItem);
    }

    @Test
    public void removeAutomationItem_RejectsNull() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("automationTimeItem");
        budget.removeAutomationTime(null);
    }

    @Test
    public void removeAutomationItem_RemovesItem() throws Exception {
        TimeBudget budget = new TimeBudget("test");

        AutomationTimeItem fridays = new AutomationTimeItem(new WorkingHours(8), AutomationInterval.FRIDAYS, new
                OpenDateRange(LocalDate.of(2017, 1, 1), null));
        AutomationTimeItem monday = new AutomationTimeItem(new WorkingHours(8), AutomationInterval.MONDAYS, new
                OpenDateRange(LocalDate.of(2017, 1, 1), null));
        AutomationTimeItem sunday = new AutomationTimeItem(new WorkingHours(4), AutomationInterval.SUNDAYS, new
                OpenDateRange(LocalDate.of(2017, 1, 1), null));

        budget.addAutomationTime(fridays);
        budget.addAutomationTime(monday);
        budget.addAutomationTime(sunday);

        budget.removeAutomationTime(sunday);

        assertThat(budget.getAutomationTimes()).containsExactlyInAnyOrder(fridays, monday).doesNotContain
                (sunday);

        assertThat(sunday.getParentBudget()).isNull();
    }

    @Test
    public void removeAutomationItem_RejectsAutomationItemsWhichDoesNotBelongToTheBudget() throws Exception {
        TimeBudget budget = new TimeBudget("test");

        TimeBudget otherBudget = new TimeBudget("other");
        AutomationTimeItem automationTimeItem = new AutomationTimeItem(new WorkingHours(8), AutomationInterval
                .ONCE, new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        otherBudget.addAutomationTime(automationTimeItem);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("AutomationTimeItem does not belong to me!");
        budget.removeAutomationTime(automationTimeItem);
    }

    @Test
    public void addManualTime_RejectsNull() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("manualTimeItem");
        budget.addManualTime(null);
    }

    @Test
    public void addManualTime_setParentBudgetToTheTimeBudget() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        ManualTimeItem manualTimeItem = new ManualTimeItem(new WorkingHours(8), new DateRange(LocalDate.of(2017, 1, 1),
                LocalDate.of(2017, 1, 1)), TimeType.DONE);
        budget.addManualTime(manualTimeItem);

        assertThat(manualTimeItem.getParentBudget()).isEqualTo(budget);
    }

    @Test
    public void getManualTimeItems_ReturnsIterableListOfCreatedManualTimeItems() throws Exception {

        TimeBudget budget = new TimeBudget("test");

        ManualTimeItem firstItem = new ManualTimeItem(new WorkingHours(2),
                new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)), TimeType.DONE);
        ManualTimeItem secondItem = new ManualTimeItem(new WorkingHours(4),
                new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)), TimeType.DONE);

        budget.addManualTime(firstItem);
        budget.addManualTime(secondItem);

        assertThat(budget.getManualItems())
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(firstItem, secondItem)
                .isInstanceOf(Iterable.class);
    }

    @Test
    public void removeManualTimeItem_RejectsNull() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("manualTimeItem");
        budget.removeManualTime(null);
    }

    @Test
    public void removeManualTimeItem_removesItem() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        ManualTimeItem first = new ManualTimeItem(new WorkingHours(8), new DateRange(LocalDate.of(2017, 1, 1),
                LocalDate.of(2017, 1, 1)), TimeType.DONE);
        ManualTimeItem second = new ManualTimeItem(new WorkingHours(8), new DateRange(LocalDate.of(2017, 1, 1),
                LocalDate.of(2017, 1, 1)), TimeType.DONE);
        ManualTimeItem thirdToBeRemoved = new ManualTimeItem(new WorkingHours(16), new DateRange(LocalDate.of(2017,
                1, 13), LocalDate.of(2017, 1, 13)), TimeType.VACATION);

        budget.addManualTime(first);
        budget.addManualTime(second);
        budget.addManualTime(thirdToBeRemoved);

        budget.removeManualTime(thirdToBeRemoved);

        assertThat(budget.getManualItems()).containsExactlyInAnyOrder(first, second).doesNotContain(thirdToBeRemoved);
        assertThat(thirdToBeRemoved.getParentBudget()).isNull();
    }

    @Test
    public void removeManualTimeItem_rejectsManualTimeItemsWhichDoesNotBelongToTheTimeBudget() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        ManualTimeItem manualTimeItem = new ManualTimeItem(new WorkingHours(9), new DateRange(LocalDate.of(2017,
                1, 1), LocalDate.of(2017, 1, 1)), TimeType.HOLIDAY);
        TimeBudget otherBudget = new TimeBudget("otherBudget");
        otherBudget.addManualTime(manualTimeItem);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("ManualTimeItem does not belong to me!");
        budget.removeManualTime(manualTimeItem);
    }

    @Test
    public void getAccumulatedAutoTimeTill_RejectsNullAsTillDate() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("tillDate");
        budget.getAccumulatedAutoTimeTill(null);
    }

    @Test
    public void getAccumulatedAutoTimeTill_ReturnsZeroIfNoAutomationItemIsSet() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        assertThat(budget.getAccumulatedAutoTimeTill(LocalDate.of(2017, 1, 1))).isEqualTo(0);
    }

    @Test
    public void getAccumulatedAutoTimeTill_ReturnsTheSumOfAllAutomationTimeItems() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        budget.addAutomationTime(new AutomationTimeItem(new WorkingHours(2), AutomationInterval.ONCE,
                new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1))));
        budget.addAutomationTime(new AutomationTimeItem(new WorkingHours(2), AutomationInterval.ONCE,
                new OpenDateRange(LocalDate.of(2017, 1, 2), LocalDate.of(2017, 1, 2))));

        assertThat(budget.getAccumulatedAutoTimeTill(LocalDate.of(2017, 1, 3))).isEqualTo(4);
    }

    @Test
    public void getManualTimeByTypeTill_RejectsNullAsTillDate() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("tillDate");
        budget.getManualTimeByTypeTill(TimeType.DONE, null);
    }

    @Test
    public void getManualTimeByType_RejectsNullAsTimeType() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("timeType");
        budget.getManualTimeByTypeTill(null, LocalDate.now());
    }

    @Test
    public void getManualTimeByType_ReturnsOnlyTheSumOfHoursForTheTimeTypeEntry() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        budget.addManualTime(new ManualTimeItem(new WorkingHours(8),
                new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)),
                TimeType.HOLIDAY));
        budget.addManualTime(new ManualTimeItem(new WorkingHours(8),
                new DateRange(LocalDate.of(2017, 1, 2), LocalDate.of(2017, 1, 2)),
                TimeType.DONE));
        budget.addManualTime(new ManualTimeItem(new WorkingHours(8),
                new DateRange(LocalDate.of(2017, 1, 6), LocalDate.of(2017, 1, 6)),
                TimeType.HOLIDAY));
        assertThat(budget.getManualTimeByTypeTill(TimeType.HOLIDAY, LocalDate.of(2017, 2, 1))).isEqualTo(16);
        assertThat(budget.getManualTimeByTypeTill(TimeType.DONE, LocalDate.of(2017, 1, 2))).isEqualTo(8);
        assertThat(budget.getManualTimeByTypeTill(TimeType.HOLIDAY, LocalDate.of(2017, 1, 2))).isEqualTo(8);
    }

    @Test
    public void getTimeBalanceTill_RejectsNullAsDate() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("tillDate");
        new TimeBudget("test").getTimeBalanceTill(null);
    }

    @Test
    public void getTimeBalanceTill_ReturnsCorrectBalanceValue() throws Exception {
        TimeBudget budget = new TimeBudget("test");
        //Each day 1 hour of work to be done.
        budget.addAutomationTime(new AutomationTimeItem(new WorkingHours(1),
                AutomationInterval.DAILY,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null)));

        //Balance for the 2017/1/1 = 0
        budget.addManualTime(new ManualTimeItem(new WorkingHours(1),
                new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)),
                TimeType.HOLIDAY));
        //Done work for the following 4 day only 2 hours instead of 4 Balance on 2017/1/5 = -2
        budget.addManualTime(new ManualTimeItem(new WorkingHours(2),
                new DateRange(LocalDate.of(2017, 1, 2), LocalDate.of(2017, 1, 5)),
                TimeType.DONE));
        //Holiday on 2017/1/6 Balance = -2
        budget.addManualTime(new ManualTimeItem(new WorkingHours(1),
                new DateRange(LocalDate.of(2017, 1, 6), LocalDate.of(2017, 1, 6)),
                TimeType.HOLIDAY));
        //Ill for the next 6 days Balance stays at -2
        budget.addManualTime(new ManualTimeItem(new WorkingHours(6),
                new DateRange(LocalDate.of(2017, 1, 7), LocalDate.of(2017, 1, 12)),
                TimeType.ILLNESS));
        //worked 5 hours straight on 2017$/1/13 new balance at 2
        budget.addManualTime(new ManualTimeItem(new WorkingHours(5),
                new DateRange(LocalDate.of(2017, 1, 13), LocalDate.of(2017, 1, 13)),
                TimeType.DONE));

        assertThat(budget.getTimeBalanceTill(LocalDate.of(2017, 1, 1))).isEqualTo(0);
        assertThat(budget.getTimeBalanceTill(LocalDate.of(2017, 1, 5))).isEqualTo(-2);
        assertThat(budget.getTimeBalanceTill(LocalDate.of(2017, 1, 6))).isEqualTo(-2);
        assertThat(budget.getTimeBalanceTill(LocalDate.of(2017, 1, 12))).isEqualTo(-2);
        assertThat(budget.getTimeBalanceTill(LocalDate.of(2017, 1, 13))).isEqualTo(2);
    }
}