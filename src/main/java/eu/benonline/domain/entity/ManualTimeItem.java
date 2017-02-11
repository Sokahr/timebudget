package eu.benonline.domain.entity;

import eu.benonline.domain.vo.DateRange;
import eu.benonline.domain.vo.TimeType;
import eu.benonline.domain.vo.WorkingHours;
import lombok.*;

import javax.persistence.*;

/**
 * Item for manual Time entries.
 * Created by Benjamin Peter.
 */
@Data
@ToString(exclude = {"parentBudget"})
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Entity
public class ManualTimeItem {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private TimeBudget parentBudget;

    @NonNull
    @Embedded
    private WorkingHours hours;

    @NonNull
    @Embedded
    private DateRange dateRange;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TimeType timeType;

}
