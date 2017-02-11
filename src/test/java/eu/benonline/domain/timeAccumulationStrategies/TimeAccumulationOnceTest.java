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
public class TimeAccumulationOnceTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void accumulateTimeReturnsZeroIfTillDateIsOutOfRange() throws Exception {
        TimeAccumulationOnce timeAccumulationOnce = new TimeAccumulationOnce();
        LocalDate tillDate = LocalDate.of(2017, 1, 1);
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 3), LocalDate.of(2017, 1, 3));
        float hoursPerInterval = 8;
        assertThat(timeAccumulationOnce.accumulateTime(hoursPerInterval, openDateRange, tillDate)).isEqualTo(0);
    }

    @Test
    public void accumulateTimeRejectsNullAsOpenDateRange() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("openDateRange");
        new TimeAccumulationOnce().accumulateTime(8, null, LocalDate.of(2017, 1, 1));
    }

    @Test
    public void accumulateTimeRejectsNullAsTillDate() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("tillDate");
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1));
        new TimeAccumulationOnce().accumulateTime(8, openDateRange, null);
    }

    @Test
    public void accumulateTimeReturnsHoursPerIntervalIfTillDateIsInRange() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 2, 1));
        LocalDate tillDate = LocalDate.of(2017, 1, 20);
        float accumulateTime = new TimeAccumulationOnce().accumulateTime(8, openDateRange, tillDate);
        assertThat(accumulateTime).isEqualTo(8);
    }

    @Test
    public void accumulateTimeReturnsHoursPerIntervalIfTillDateIsAfterRangeTillDate() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 2));
        LocalDate tillDate = LocalDate.of(2018, 1, 1);
        float actual = new TimeAccumulationOnce().accumulateTime(8, openDateRange, tillDate);
        assertThat(actual).isEqualTo(8);
    }
}