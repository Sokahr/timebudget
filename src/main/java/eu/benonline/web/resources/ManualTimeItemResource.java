package eu.benonline.web.resources;

import eu.benonline.domain.vo.TimeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDate;

/**
 * Created by Benjamin Peter.
 */
@Relation(value = "manualTimeItem", collectionRelation = "manualTimeItems")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManualTimeItemResource extends ResourceSupport {
   float hours;
   LocalDate fromDate;
   LocalDate tillDate;
   TimeType timeType;
}
