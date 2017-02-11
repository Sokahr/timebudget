package eu.benonline.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.beans.ConstructorProperties;
import java.time.LocalDate;

/**
 * This is a DateRange where the tillDate is optional, so you could define a beginDate without knowing the end-date.
 * When the tillDate is set the dates will be validated so the tillDate could not be before the beginDate but on the same
 * day.
 * <p>
 * Created by Benjamin Peter.
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Embeddable
public class OpenDateRange {
    @Column(nullable = false)
    @NonNull
    LocalDate beginDate;

    LocalDate tillDate;

    @ConstructorProperties({"beginDate", "endDate"})
    public OpenDateRange(@NonNull LocalDate beginDate, LocalDate tillDate) {
        if (tillDate != null)
            Assert.isTrue(beginDate.isBefore(tillDate.plusDays(1)), "tillDate could not be before beginDate, Time-traveler");
        this.beginDate = beginDate;
        this.tillDate = tillDate;
    }

    public boolean isDateInRange(@NonNull LocalDate date) {
        if (this.tillDate == null)
            return beginDate.isBefore(date.plusDays(1));
        else
            return (beginDate.isBefore(date.plusDays(1)) && tillDate.isAfter(date.minusDays(1)));
    }
}
