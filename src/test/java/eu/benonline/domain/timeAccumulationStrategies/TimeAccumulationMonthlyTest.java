package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by Benjamin Peter.
 */
public class TimeAccumulationMonthlyTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void returnsSumOfHoursOnlyInsideTheDateRange() throws Exception {
        TimeAccumulationMonthly timeAccumulationMonthly = new TimeAccumulationMonthly();
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 2), LocalDate.of(2017, 5, 2));
        LocalDate tillDate = LocalDate.of(2017, 6, 2);

        assertThat(timeAccumulationMonthly.accumulateTime(2, openDateRange, tillDate)).isEqualTo(10);
    }

    @Test
    public void returnsSumOfHoursBetweenBeginDateAndTillDate() throws Exception {
        TimeAccumulationMonthly timeAccumulationMonthly = new TimeAccumulationMonthly();
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 7), null);
        LocalDate tillDate = LocalDate.of(2017, 4, 6);

        assertThat(timeAccumulationMonthly.accumulateTime(8, openDateRange, tillDate)).isEqualTo(24);
    }

    @Test
    public void returnsZeroIfTillDateIsBeforeRangeBegin() throws Exception {
        TimeAccumulationMonthly timeAccumulationMonthly = new TimeAccumulationMonthly();
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 7), null);
        LocalDate tillDate = LocalDate.of(2017, 1, 6);

        assertThat(timeAccumulationMonthly.accumulateTime(80, openDateRange, tillDate)).isEqualTo(0);
    }

    @Test
    public void rejectsNullForTillDate() throws Exception {
        TimeAccumulationMonthly timeAccumulationMonthly = new TimeAccumulationMonthly();
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.now(), null);

        thrown.expect(NullPointerException.class);
        thrown.expectMessage("tillDate");
        timeAccumulationMonthly.accumulateTime(1, openDateRange, null);
    }

    @Test
    public void rejectsNullForOpenRange() throws Exception {
        TimeAccumulationMonthly timeAccumulationMonthly = new TimeAccumulationMonthly();

        thrown.expect(NullPointerException.class);
        thrown.expectMessage("openDateRange");

        timeAccumulationMonthly.accumulateTime(1, null, LocalDate.now());
    }
}