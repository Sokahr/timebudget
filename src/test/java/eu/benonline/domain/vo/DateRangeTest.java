package eu.benonline.domain.vo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UnitTest for {@link DateRange}
 * Created by Benjamin Peter
 */
@SuppressWarnings("unused")
public class DateRangeTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorRejectsNullAsFromDate() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("fromDate");
        new DateRange(null, LocalDate.now());

    }

    @Test
    public void constructorRejectsNullAsTillDate() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("tillDate");
        new DateRange(LocalDate.now(), null);
    }

    @Test
    public void constructorRejectsFromDateAfterTillDate() throws Exception {
        this.thrown.expect(IllegalArgumentException.class);
        this.thrown.expectMessage("From date cannot be after the till date time traveler");
        new DateRange(LocalDate.of(2017, 1, 13), LocalDate.of(2017, 1, 12));
    }

    @Test
    public void constructorSetsDatesCorrect() throws Exception {
        LocalDate fromDate = LocalDate.of(2017, 1, 12);
        LocalDate tillDate = LocalDate.of(2017, 1, 14);
        DateRange dateRange = new DateRange(fromDate, tillDate);
        assertThat(dateRange.getFromDate()).isEqualTo(fromDate);
        assertThat(dateRange.getTillDate()).isEqualTo(tillDate);
    }

    @Test
    public void constructorAcceptsFromAndTillTheSameDate() throws Exception {
        LocalDate date = LocalDate.of(2017, 1, 14);
        new DateRange(date, date);
    }

    @Test
    public void isDateInRangeReturnsTrueIfDateIsEqualFromDate() throws Exception {
        DateRange dateRange = new DateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 12));
        assertThat(dateRange.isDateInRange(LocalDate.of(2017, 1, 10))).isTrue();
    }

    @Test
    public void isDateInRangeReturnsTrueIfDateIsEqualTillDate() throws Exception {
        DateRange dateRange = new DateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 12));
        assertThat(dateRange.isDateInRange(LocalDate.of(2017, 1, 12))).isTrue();
    }

    @Test
    public void isDateInRangeReturnsTrueIfDateIsBetweenFromAndTillDate() throws Exception {
        DateRange dateRange = new DateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 12));
        assertThat(dateRange.isDateInRange(LocalDate.of(2017, 1, 11))).isTrue();
    }

    @Test
    public void isDateInRangeReturnsFalseIfDateIsBeforeFromDate() throws Exception {
        DateRange dateRange = new DateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 12));
        assertThat(dateRange.isDateInRange(LocalDate.of(2017, 1, 9))).isFalse();
    }

    @Test
    public void isDateInRangeReturnsFalseIfDateIsAfterTillDate() throws Exception {
        DateRange dateRange = new DateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 12));
        assertThat(dateRange.isDateInRange(LocalDate.of(2017, 1, 13))).isFalse();
    }

    @Test
    public void isDateInRangeRejectsNullAsDate() throws Exception {
        DateRange dateRange = new DateRange(LocalDate.of(2017, 1, 10), LocalDate.of(2017, 1, 12));
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("date");
        dateRange.isDateInRange(null);
    }
}