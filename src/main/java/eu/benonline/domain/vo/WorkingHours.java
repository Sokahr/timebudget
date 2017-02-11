package eu.benonline.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.beans.ConstructorProperties;

/**
 * Working value is a ValueObject with a float which could not be less then 0.
 * <p>
 * Created by Benjamin Peter.
 */
@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Embeddable
public class WorkingHours {
    @Column(name = "WORKING_HOURS", nullable = false)
    final float value;

    @ConstructorProperties({"value"})
    public WorkingHours(float value) {
        Assert.isTrue(value > 0, "value must be larger then 0");
        this.value = value;
    }
}
