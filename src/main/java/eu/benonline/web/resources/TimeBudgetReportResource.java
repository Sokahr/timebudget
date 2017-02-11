package eu.benonline.web.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Created by Benjamin.
 */
@Relation(value = "timeBudgetReport", collectionRelation = "timeBudgetReports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class TimeBudgetReportResource extends ResourceSupport {
    String budgetName;
    float balance;
}
