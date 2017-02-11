package eu.benonline.web;

import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.service.TimeBudgetService;
import eu.benonline.web.resources.TimeBudgetResource;
import eu.benonline.web.resources.TimeBudgetResourceAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by Benjamin Peter.
 */
@RestController
@RequestMapping("/time_budget")
@RequiredArgsConstructor
@ExposesResourceFor(TimeBudgetResource.class)
public class TimeBudgetController {

    private final TimeBudgetService timeBudgetService;

    @PostMapping
    public ResponseEntity<Void> createTimeBudget(@RequestBody TimeBudgetResource budget) {
        TimeBudget createdBudget = timeBudgetService.createNewTimeBudgetWithName(budget.getBudgetName());
        if (createdBudget != null) {
            return ResponseEntity.created(linkTo(TimeBudgetController.class)
                    .slash(createdBudget.getId()).toUri()).build();
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping(path = "/{timeBudgetId}")
    public ResponseEntity<Void> deleteTimeBudget(@PathVariable("timeBudgetId") long timeBudgetId) {
        timeBudgetService.deleteBudgetWithId(timeBudgetId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{timeBudgetId}")
    public ResponseEntity<TimeBudgetResource> updateTimeBudget(@RequestBody TimeBudgetResource timeBudgetResource,
                                                               @PathVariable("timeBudgetId") long budgetId) {

        TimeBudget timeBudget = timeBudgetService.updateTimeBudgetNameForBudgetWithId(
                timeBudgetResource.getBudgetName(), budgetId);
        if (timeBudget == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.accepted().body(new TimeBudgetResourceAssembler().toResource(timeBudget));
    }
}
