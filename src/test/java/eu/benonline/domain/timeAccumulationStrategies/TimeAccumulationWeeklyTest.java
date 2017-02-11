package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Benjamin Peter.
 */
public class TimeAccumulationWeeklyTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void calculationReturnsTheSumOfHoursFromBeginRangeTillTillDate() throws Exception {
        TimeAccumulationWeekly timeAccumulationWeekly = new TimeAccumulationWeekly(DayOfWeek.MONDAY);
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), null);
        LocalDate tillDate = LocalDate.of(2017, 1, 25);
        float accumulatedTime = timeAccumulationWeekly.accumulateTime(2, openDateRange, tillDate);
        assertThat(accumulatedTime).isEqualTo(8); //4 times monday 2 hours each.
    }

    @Test
    public void calculationReturnsTheSumOfHoursInsideTheRangeOnlyIfTillDateIsAfterRange() throws Exception {
        TimeAccumulationWeekly timeAccumulationWeekly = new TimeAccumulationWeekly(DayOfWeek.SUNDAY);
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 2, 5)); //6 Sundays
        LocalDate tillDate = LocalDate.of(2017, 2, 25);
        float accumulatedTime = timeAccumulationWeekly.accumulateTime(4, openDateRange, tillDate);
        assertThat(accumulatedTime).isEqualTo(24); //6 times sunday 4 hours each.
    }

    @Test
    public void calculationReturnsZeroIfTillDateIsBeforeBeginDate() throws Exception {
        TimeAccumulationWeekly timeAccumulationWeekly = new TimeAccumulationWeekly(DayOfWeek.FRIDAY);
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 2, 5));
        LocalDate tillDate = LocalDate.of(2016, 12, 25);
        float accumulatedTime = timeAccumulationWeekly.accumulateTime(8, openDateRange, tillDate);
        assertThat(accumulatedTime).isEqualTo(0);
    }

    @Test
    public void calculationRejectsNullAsTillDate() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("tillDate");
        TimeAccumulationWeekly timeAccumulationWeekly = new TimeAccumulationWeekly(DayOfWeek.SATURDAY);
        timeAccumulationWeekly.accumulateTime(2, new OpenDateRange(LocalDate.of(2017, 1, 1), null), null);

    }

    @Test
    public void calculationRejectsNullAsOpenDateRange() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("openDateRange");
        TimeAccumulationWeekly timeAccumulationWeekly = new TimeAccumulationWeekly(DayOfWeek.FRIDAY);
        timeAccumulationWeekly.accumulateTime(3, null, LocalDate.of(2017, 1, 2));
    }

    @Test
    public void constructorRejectsNullAsDayOfWeek() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("dayOfWeek");
        new TimeAccumulationWeekly(null);

    }
}