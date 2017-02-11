package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Benjamin Peter.
 */
public class TimeAccumulationDaily implements TimeAccumulationStrategy {
    @Override
    public float accumulateTime(float hoursPerInterval, @NonNull OpenDateRange openDateRange, @NonNull LocalDate tillDate) {

        if (openDateRange.getBeginDate().isAfter(tillDate))
            return 0;
        if (openDateRange.isDateInRange(tillDate))
            return hoursPerInterval * openDateRange.getBeginDate().until(tillDate.plusDays(1), ChronoUnit.DAYS);
        else
            return hoursPerInterval * openDateRange.getBeginDate().until(openDateRange.getTillDate().plusDays(1), ChronoUnit.DAYS);

    }
}
