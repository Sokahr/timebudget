package eu.benonline.web.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Created by Benjamin Peter.
 */
@Relation(value = "timeBudget", collectionRelation = "timeBudgets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeBudgetResource extends ResourceSupport {
    long budgetId;
    String budgetName;

}

