package eu.benonline.domain.vo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link WorkingHours}
 * Created by Benjamin Peter.
 */
public class WorkingHoursTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorRejectsHoursLessThenZero() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("value must be larger then 0");
        new WorkingHours(-1);
    }

    @Test
    public void constructorRejectsHoursEqualZero() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("value must be larger then 0");
        new WorkingHours(0);
    }

    @Test
    public void constructorAcceptsValidValueLargerThenZero() throws Exception {
        WorkingHours hours = new WorkingHours(0.1f);
        assertThat(hours.getValue()).isEqualTo(0.1f);

    }
}