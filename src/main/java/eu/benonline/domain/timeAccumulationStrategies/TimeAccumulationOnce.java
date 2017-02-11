package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * Accumulation for the {@link eu.benonline.domain.vo.AutomationInterval} ONCE
 * Created by Benjamin Peter.
 */
public class TimeAccumulationOnce implements TimeAccumulationStrategy {
    @Override
    public float accumulateTime(float hoursPerInterval, @NonNull OpenDateRange openDateRange, @NonNull LocalDate tillDate) {
        if (openDateRange.getBeginDate().isBefore(tillDate) || openDateRange.getBeginDate().isEqual(tillDate))
            return hoursPerInterval;
        else
            return 0;
    }
}
