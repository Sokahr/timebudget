package eu.benonline.domain.entity;

import eu.benonline.domain.timeAccumulationStrategies.AccumulationFactory;
import eu.benonline.domain.vo.AutomationInterval;
import eu.benonline.domain.vo.OpenDateRange;
import eu.benonline.domain.vo.WorkingHours;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Benjamin Peter.
 */
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(exclude = {"parentBudget"})
@Entity
public class AutomationTimeItem {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private TimeBudget parentBudget;

    @NonNull
    @Embedded
    private WorkingHours hours;

    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AutomationInterval interval;

    @NonNull
    @Embedded
    private OpenDateRange openDateRange;

    public float calculateAccumulatedTimeTill(@NonNull LocalDate date) {
        return AccumulationFactory
                .createAccumulationStrategy(interval)
                .accumulateTime(
                        hours.getValue(),
                        openDateRange,
                        date);
    }
}
