package eu.benonline.web.resources;

import eu.benonline.domain.vo.AutomationInterval;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDate;

/**
 * Created by Benjamin Peter.
 */
@Relation(value = "automationTimeItem", collectionRelation = "automationTimeItems")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutomationTimeItemResource extends ResourceSupport {
    float hours;
    @NonNull
    AutomationInterval interval;
    @NonNull
    LocalDate beginDate;
    LocalDate tillDate;
}
