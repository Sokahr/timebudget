package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.OpenDateRange;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Created by Benjamin Peter.
 */
public class TimeAccumulationWeekly implements TimeAccumulationStrategy {
    private final DayOfWeek dayOfWeek;

    public TimeAccumulationWeekly(@NonNull DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public float accumulateTime(float hoursPerInterval, @NonNull OpenDateRange openDateRange, @NonNull LocalDate tillDate) {
        if (tillDate.isBefore(openDateRange.getBeginDate()))
            return 0;

        if (!openDateRange.isDateInRange(tillDate))
            tillDate = openDateRange.getTillDate();

        tillDate = tillDate.plusDays(1);
        float accumulatedHours = 0;
        LocalDate currentDate = openDateRange.getBeginDate();
        do {
            if (currentDate.getDayOfWeek() == dayOfWeek) {
                accumulatedHours += hoursPerInterval;
                currentDate = currentDate.plusWeeks(1);
            } else {
                currentDate = currentDate.plusDays(1);
            }
        } while (currentDate.isBefore(tillDate));

        return accumulatedHours;
    }
}
