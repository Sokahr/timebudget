package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.AutomationInterval;
import lombok.extern.java.Log;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Benjamin Peter.
 */
@Log
public class AccumulationFactoryTest {
    @Test
    public void factoryReturnsAStrategyForAllAutomationIntervals() throws Exception {
        for (AutomationInterval interval :
                AutomationInterval.values()) {
            assertThat(AccumulationFactory
                    .createAccumulationStrategy(interval))
                    .as("returns strategy for %s", interval.toString())
                    .isInstanceOf(TimeAccumulationStrategy.class);
        }

    }
}