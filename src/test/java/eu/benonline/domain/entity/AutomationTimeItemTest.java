package eu.benonline.domain.entity;

import eu.benonline.domain.timeAccumulationStrategies.AccumulationFactory;
import eu.benonline.domain.timeAccumulationStrategies.TimeAccumulationOnce;
import eu.benonline.domain.vo.AutomationInterval;
import eu.benonline.domain.vo.OpenDateRange;
import eu.benonline.domain.vo.WorkingHours;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Benjamin Peter
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AccumulationFactory.class)
public class AutomationTimeItemTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private WorkingHours hours;
    private AutomationInterval interval;
    private OpenDateRange openDateRange;
    private AutomationTimeItem automationTimeItem;

    @Before
    public void setUp() throws Exception {
        hours = new WorkingHours(8);
        interval = AutomationInterval.ONCE;
        openDateRange = new OpenDateRange(LocalDate.now(), null);
        automationTimeItem = new AutomationTimeItem(hours, interval, openDateRange);
    }

    @Test
    public void constructorSetsParametersCorrect() throws Exception {
        assertThat(automationTimeItem.getHours()).isEqualTo(this.hours);
        assertThat(automationTimeItem.getOpenDateRange()).isEqualTo(this.openDateRange);
        assertThat(automationTimeItem.getInterval()).isEqualTo(this.interval);
    }

    @Test
    public void constructorRejectsNullAsHours() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("hours");
        new AutomationTimeItem(null, interval, openDateRange);
    }

    @Test
    public void constructorRejectsNullAsInterval() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("interval");
        new AutomationTimeItem(hours, null, openDateRange);
    }

    @Test
    public void constructorRejectsNullAsOpenDateRange() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("openDateRange");
        new AutomationTimeItem(hours, interval, null);
    }

    @Test
    public void setHoursRejectsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("hours");
        automationTimeItem.setHours(null);
    }

    @Test
    public void setIntervalRejectsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("interval");
        automationTimeItem.setInterval(null);
    }

    @Test
    public void setOpenDateRangeRejectsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("openDateRange");
        automationTimeItem.setOpenDateRange(null);
    }

    @Test
    public void calculateAccumulatedTimeRejectsNull() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("date");
        automationTimeItem.calculateAccumulatedTimeTill(null);
    }

    @Test
    public void calculateAccumulatedTimeCallsFactoryAndCalculationMethodOnStrategyObject() throws Exception {
        final TimeAccumulationOnce timeAccumulationOnce = mock(TimeAccumulationOnce.class);
        PowerMockito.mockStatic(AccumulationFactory.class);
        PowerMockito.when(AccumulationFactory.createAccumulationStrategy(AutomationInterval.ONCE))
                .thenReturn(timeAccumulationOnce);

        PowerMockito.when(timeAccumulationOnce,
                "accumulateTime",
                hours.getValue(),
                openDateRange,
                LocalDate.of(2017, 1, 1)).thenReturn(42.0f);

        assertThat(automationTimeItem.calculateAccumulatedTimeTill(LocalDate.of(2017, 1, 1))).isEqualTo(42.0f);
    }
}

