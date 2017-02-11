package eu.benonline.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.util.Assert;

import javax.persistence.Embeddable;
import java.beans.ConstructorProperties;
import java.time.LocalDate;

/**
 * DateRange is a ValueObject works similar to {@link OpenDateRange}
 * with the exception that tillDate is mandatory.
 *
 * Created by Benjamin Peter
 */
@Value
@Embeddable
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class DateRange {
    @NonNull
    private LocalDate fromDate;
    @NonNull
    private LocalDate tillDate;

    @ConstructorProperties({"fromDate", "tillDate"})
    public DateRange(@NonNull LocalDate fromDate, @NonNull LocalDate tillDate) {
        Assert.isTrue(fromDate.isBefore(tillDate.plusDays(1)), "From date cannot be after the till date time traveler");
        this.fromDate = fromDate;
        this.tillDate = tillDate;
    }

    public boolean isDateInRange(@NonNull LocalDate date) {
        return (fromDate.isBefore(date.plusDays(1)) && tillDate.isAfter(date.minusDays(1)));
    }
}
