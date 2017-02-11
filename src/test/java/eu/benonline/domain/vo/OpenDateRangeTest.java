package eu.benonline.domain.vo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Test for {@link OpenDateRange}
 * Created by Benjamin Peter.
 */
public class OpenDateRangeTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorRejectsNullAsBeginDate() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("beginDate");

        new OpenDateRange(null, null);
    }

    @Test
    public void constructorAcceptsNullAsTillDate() throws Exception {
        new OpenDateRange(LocalDate.now(), null);
    }

    @Test
    public void constructorAcceptsEndDateEqualToBeginDate() throws Exception {
        new OpenDateRange(LocalDate.of(2017, 1, 19), LocalDate.of(2017, 1, 19));
    }

    @Test
    public void constructorRejectsTillDateEarlierThenBeginDate() throws Exception {
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("tillDate could not be before beginDate, Time-traveler");
        new OpenDateRange(LocalDate.of(2017, 1, 19), LocalDate.of(2017, 1, 17));
    }

    @Test
    public void constructorSetsDatesCorrect() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 12), LocalDate.of(2017, 2, 28));

        assertThat(openDateRange.getBeginDate()).isEqualTo(LocalDate.of(2017, 1, 12));
        assertThat(openDateRange.getTillDate()).isEqualTo(LocalDate.of(2017, 2, 28));
    }

    @Test
    public void isDateInRangeReturnsTrueIfDateIsEqualToFromDate() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 10), null);
        assertThat(openDateRange.isDateInRange(LocalDate.of(2017, 1, 10))).isTrue();
    }

    @Test
    public void isDateInRangeReturnsTrueIfDateIsAfterFromDateAndTillDateIsNull() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 10), null);
        assertThat(openDateRange.isDateInRange(LocalDate.of(2017, 2, 10))).isTrue();
    }

    @Test
    public void isDateInRangeReturnsTrueIfDateIsEqualToTillDate() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 20));
        assertThat(openDateRange.isDateInRange(LocalDate.of(2017, 1, 20))).isTrue();
    }

    @Test
    public void isDateInRangeReturnsTrueIfDateIsBetweenFromAndTillDate() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 20));
        assertThat(openDateRange.isDateInRange(LocalDate.of(2017, 1, 16))).isTrue();
    }

    @Test
    public void isDateInRangeReturnsFalseIfDateIsBeforeFromDate() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 10), null);
        assertThat(openDateRange.isDateInRange(LocalDate.of(2017, 1, 9))).isFalse();
    }

    @Test
    public void isDateInRangeReturnsFalseIfDateIsAfterTillDate() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 2));
        assertThat(openDateRange.isDateInRange(LocalDate.of(2017, 1, 3)));
    }

    @Test
    public void isDateInRangeRejectsNullAsDate() throws Exception {
        OpenDateRange openDateRange = new OpenDateRange(LocalDate.now(), null);
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("date");
        openDateRange.isDateInRange(null);
    }
}