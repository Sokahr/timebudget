package eu.benonline.domain.entity;

import eu.benonline.domain.vo.DateRange;
import eu.benonline.domain.vo.TimeType;
import eu.benonline.domain.vo.WorkingHours;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * UnitTest for {@link ManualTimeItem}
 * Created by Benjamin Peter on 21.01.2017.
 */
public class ManualTimeItemTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private WorkingHours hours;
    private TimeType timeType;
    private DateRange dateRange;
    private ManualTimeItem manualTimeItem;

    @Before
    public void setUp() throws Exception {
        hours = new WorkingHours(8);
        timeType = TimeType.ILLNESS;
        dateRange = new DateRange(LocalDate.of(2017, 1, 17), LocalDate.of(2017, 1, 17));
        manualTimeItem = new ManualTimeItem(hours, dateRange, timeType);
    }

    @Test
    public void constructor_SetsParameterCorrects() throws Exception {
        assertThat(manualTimeItem.getHours()).isEqualTo(hours);
        assertThat(manualTimeItem.getTimeType()).isEqualTo(timeType);
        assertThat(manualTimeItem.getDateRange()).isEqualTo(dateRange);
    }

    @Test
    public void constructor_RejectsNullForHours() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("hours");

        new ManualTimeItem(null, dateRange, timeType);
    }

    @Test
    public void constructor_ejectsNullForDateRange() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("dateRange");

        new ManualTimeItem(hours, null, timeType);
    }

    @Test
    public void constructor_RejectsNullForTimeType() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("timeType");

        new ManualTimeItem(hours, dateRange, null);
    }

    @Test
    public void setHours_RejectsNull() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("hours");
        manualTimeItem.setHours(null);
    }

    @Test
    public void setDateRange_RejectsNull() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("dateRange");
        manualTimeItem.setDateRange(null);
    }

    @Test
    public void setTimeType_RejectsNull() throws Exception {
        this.thrown.expect(NullPointerException.class);
        this.thrown.expectMessage("timeType");
        manualTimeItem.setTimeType(null);
    }
}