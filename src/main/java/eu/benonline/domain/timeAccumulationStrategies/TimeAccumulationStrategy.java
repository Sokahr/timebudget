package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;

import java.time.LocalDate;

/**
 * Created by Benjamin Peter.
 */
public interface TimeAccumulationStrategy {
    float accumulateTime(float hoursPerInterval, OpenDateRange openDateRange, LocalDate tillDate);
}
