package eu.benonline.domain.entity;

import eu.benonline.domain.vo.TimeType;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Entity holding TimeBudget information.
 */
@Data
@Entity
public class TimeBudget {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "parentBudget", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<AutomationTimeItem> automationTimeItems = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "parentBudget", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<ManualTimeItem> manualTimeItems = new ArrayList<>();

    @SuppressWarnings("unused")
    private TimeBudget() {
        //needed for Hibernate to work
    }

    @ConstructorProperties({"name"})
    public TimeBudget(@NonNull String name) {
        setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String name) {
        Assert.hasText(name, "Name cannot be empty");
        this.name = name;
    }

    public boolean addAutomationTime(@NonNull AutomationTimeItem automationTimeItem) {
        automationTimeItem.setParentBudget(this);
        return automationTimeItems.add(automationTimeItem);
    }

    public Iterable<AutomationTimeItem> getAutomationTimes() {
        return automationTimeItems;
    }

    public void removeAutomationTime(@NonNull AutomationTimeItem automationTimeItem) {
        Assert.isTrue(automationTimeItem.getParentBudget().equals(this), "AutomationTimeItem does not belong to me!");
        automationTimeItem.setParentBudget(null);
        automationTimeItems.remove(automationTimeItem);
    }

    public boolean addManualTime(@NonNull ManualTimeItem manualTimeItem) {
        manualTimeItem.setParentBudget(this);
        return manualTimeItems.add(manualTimeItem);
    }

    public Iterable<ManualTimeItem> getManualItems() {
        return manualTimeItems;
    }

    public void removeManualTime(@NonNull ManualTimeItem manualTimeItem) {
        Assert.isTrue(manualTimeItem.getParentBudget().equals(this), "ManualTimeItem does not belong to me!");
        manualTimeItem.setParentBudget(null);
        manualTimeItems.remove(manualTimeItem);
    }

    float getAccumulatedAutoTimeTill(@NonNull LocalDate tillDate) {
        float accumulatedTime = 0.0f;
        for (AutomationTimeItem automationTimeItem : automationTimeItems
                ) {
            accumulatedTime += automationTimeItem.calculateAccumulatedTimeTill(tillDate);
        }
        return accumulatedTime;
    }

    float getManualTimeByTypeTill(@NonNull TimeType timeType, @NonNull LocalDate tillDate) {
        float accumulatedTime = 0.0f;
        for (ManualTimeItem manualTimeItem :
                manualTimeItems) {
            if (manualTimeItem.getTimeType() == timeType
                    && (manualTimeItem.getDateRange().getFromDate().isBefore(tillDate.plusDays(1)))) {
                accumulatedTime += manualTimeItem.getHours().getValue();
            }
        }
        return accumulatedTime;
    }

    public float getTimeBalanceTill(@NonNull LocalDate tillDate) {
        float automationTime = getAccumulatedAutoTimeTill(tillDate);
        float manualTime = 0;
        for (ManualTimeItem manualTimeItem :
                manualTimeItems) {
            if (manualTimeItem.getDateRange().getFromDate().isBefore(tillDate.plusDays(1))) {
                manualTime += manualTimeItem.getHours().getValue();
            }
        }
        return manualTime - automationTime;
    }
}
