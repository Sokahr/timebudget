package eu.benonline.web;

import eu.benonline.domain.entity.AutomationTimeItem;
import eu.benonline.domain.service.TimeBudgetAutomationTimeItemService;
import eu.benonline.domain.vo.OpenDateRange;
import eu.benonline.domain.vo.WorkingHours;
import eu.benonline.web.resources.AutomationTimeItemResource;
import eu.benonline.web.resources.AutomationTimeItemResourceAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by Benjamin Peter.
 */
@RestController
@RequestMapping("/time_budget/{timeBudgetId}")
@RequiredArgsConstructor
public class TimeBudgetAutomationItemController {

    private final TimeBudgetAutomationTimeItemService timeBudgetAutomationTimeItemService;

    @PostMapping("/automation")
    public HttpEntity<Void> newAutomationTimeItem(@RequestBody AutomationTimeItemResource automationTimeItemResource,
                                                  @PathVariable("timeBudgetId") long timeBudgetId) {

        AutomationTimeItem automationTimeItem = new AutomationTimeItem(
                new WorkingHours(automationTimeItemResource.getHours()),
                automationTimeItemResource.getInterval(),
                new OpenDateRange(automationTimeItemResource.getBeginDate(), automationTimeItemResource.getTillDate()));

        AutomationTimeItem createdAutomationItem = timeBudgetAutomationTimeItemService
                .addNewAutomationTimeItemToBudgetWithId(automationTimeItem, timeBudgetId);

        if (createdAutomationItem == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.created(
                linkTo(TimeBudgetAutomationItemController.class, timeBudgetId)
                        .slash("automation")
                        .slash(createdAutomationItem.getId()).toUri()).build();
    }

    @GetMapping(path = "/automations")
    public HttpEntity<PagedResources<AutomationTimeItemResource>>
    getAutomationTimeItems(@PathVariable("timeBudgetId") long timeBudgetId, @PageableDefault Pageable pageable,
                           PagedResourcesAssembler<AutomationTimeItem> pagedResourcesAssembler,
                           AutomationTimeItemResourceAssembler automationTimeItemResourceAssembler) {
        Page<AutomationTimeItem> automationTimeItems =
                timeBudgetAutomationTimeItemService.getAutomationTimeItemsForBudgetWithId(timeBudgetId, pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toResource(automationTimeItems,
                automationTimeItemResourceAssembler));
    }

    @DeleteMapping(path = "/automation/{automationTimeItemId}")
    public HttpEntity<Void> deleteAutomationTimeItem(@PathVariable("automationTimeItemId") long automationTimeItemId) {
        timeBudgetAutomationTimeItemService.removeAutomationTimeItemWithId(automationTimeItemId);
        return ResponseEntity.noContent().build();
    }
}
