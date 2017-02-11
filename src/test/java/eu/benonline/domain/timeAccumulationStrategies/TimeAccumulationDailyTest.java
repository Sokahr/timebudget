package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Benjamin Peter.
 */
public class TimeAccumulationDailyTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private TimeAccumulationDaily timeAccumulationDaily;

    @Before
    public void setUp() throws Exception {
        timeAccumulationDaily = new TimeAccumulationDaily();
    }

    @Test
    public void calculatesTheCorrectTime() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 12));
        LocalDate tillDate = LocalDate.of(2017, 1, 7);
        assertThat(timeAccumulationDaily.accumulateTime(3, openDateRange, tillDate))
                .isEqualTo(21);
    }

    @Test
    public void calculationRejectsNullForOpenDateRange() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("openDateRange");

        timeAccumulationDaily.accumulateTime(8, null, LocalDate.of(2017, 1, 1));
    }

    @Test
    public void calculationRejectsNullForTillDate() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("tillDate");
        timeAccumulationDaily.accumulateTime(1, new OpenDateRange(LocalDate.of(2017, 1, 1), null), null);
    }

    @Test
    public void calculationReturnsZeroIfTillDateIsBeforeDateRange() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 10), null);
        LocalDate tillDate = LocalDate.of(2017, 1, 1);
        float actual = timeAccumulationDaily.accumulateTime(1, openDateRange, tillDate);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    public void calculationReturnsOnlyTheTimeInsideTheRange() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 4));
        LocalDate tillDate = LocalDate.of(2017, 1, 20);
        float actual = timeAccumulationDaily.accumulateTime(1, openDateRange, tillDate);
        assertThat(actual).isEqualTo(4);
    }
}