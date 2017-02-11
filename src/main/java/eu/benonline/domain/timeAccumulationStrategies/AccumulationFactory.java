package eu.benonline.domain.timeAccumulationStrategies;

import eu.benonline.domain.vo.AutomationInterval;
import lombok.NonNull;

import java.time.DayOfWeek;

/**
 * Created by Benjamin Peter.
 */
public class AccumulationFactory {
    public static TimeAccumulationStrategy createAccumulationStrategy(@NonNull AutomationInterval interval) {
        TimeAccumulationStrategy strategy = null;
        switch (interval) {
            case ONCE:
                strategy = new TimeAccumulationOnce();
                break;
            case DAILY:
                strategy = new TimeAccumulationDaily();
                break;
            case MONDAYS:
                strategy = new TimeAccumulationWeekly(DayOfWeek.MONDAY);
                break;
            case TUESDAYS:
                strategy = new TimeAccumulationWeekly(DayOfWeek.TUESDAY);
                break;
            case WEDNESDAYS:
                strategy = new TimeAccumulationWeekly(DayOfWeek.WEDNESDAY);
                break;
            case THURSDAYS:
                strategy = new TimeAccumulationWeekly(DayOfWeek.THURSDAY);
                break;
            case FRIDAYS:
                strategy = new TimeAccumulationWeekly(DayOfWeek.FRIDAY);
                break;
            case SATURDAYS:
                strategy = new TimeAccumulationWeekly(DayOfWeek.SATURDAY);
                break;
            case SUNDAYS:
                strategy = new TimeAccumulationWeekly(DayOfWeek.SUNDAY);
                break;
            case MONTHLY:
                strategy = new TimeAccumulationMonthly();
                break;

        }
        return strategy;
    }
}
