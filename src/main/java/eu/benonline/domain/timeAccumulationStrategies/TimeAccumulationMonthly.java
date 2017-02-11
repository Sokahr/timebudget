package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;
import lombok.NonNull;

import java.time.LocalDate;

/**
 * Created by Benjamin Peter.
 */
public class TimeAccumulationMonthly implements TimeAccumulationStrategy {
    @Override
    public float accumulateTime(float hoursPerInterval, @NonNull OpenDateRange openDateRange, @NonNull LocalDate tillDate) {
        if (tillDate.isBefore(openDateRange.getBeginDate()))
            return 0;

        if (!openDateRange.isDateInRange(tillDate))
            tillDate = openDateRange.getTillDate();

        float accumulatedHours = 0;
        LocalDate currentDate = openDateRange.getBeginDate();
        do {
            accumulatedHours += hoursPerInterval;
            currentDate = currentDate.plusMonths(1);
        } while (currentDate.isBefore(tillDate.plusDays(1)));

        return accumulatedHours;
    }
}
