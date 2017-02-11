package eu.benonline.web;

import eu.benonline.domain.entity.ManualTimeItem;
import eu.benonline.domain.service.TimeBudgetManualTimeItemService;
import eu.benonline.domain.vo.DateRange;
import eu.benonline.domain.vo.WorkingHours;
import eu.benonline.web.resources.ManualTimeItemResource;
import eu.benonline.web.resources.ManualTimeItemResourceAssembler;
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
public class TimeBudgetManualItemController {

    private final TimeBudgetManualTimeItemService manualTimeItemService;

    @PostMapping("/manual_item")
    public HttpEntity<Void> addNewManualTimeItem(@PathVariable("timeBudgetId") long timeBudgetId,
                                                 @RequestBody ManualTimeItemResource manualTimeItemResource) {
        ManualTimeItem createdManualTimeItem = new ManualTimeItem(new WorkingHours(manualTimeItemResource.getHours()),
                new DateRange(manualTimeItemResource.getFromDate(), manualTimeItemResource.getTillDate()),
                manualTimeItemResource.getTimeType());

        createdManualTimeItem = manualTimeItemService.addManualTimeItemToBudgetWithID(createdManualTimeItem,
                timeBudgetId);
        return ResponseEntity.created(
                linkTo(TimeBudgetManualItemController.class, timeBudgetId)
                        .slash("manual_item")
                        .slash(createdManualTimeItem.getId())
                        .toUri()).build();
    }

    @GetMapping("/manual_items")
    public HttpEntity<PagedResources<ManualTimeItemResource>> getAllManualItemsForBudget(@PathVariable("timeBudgetId") long timeBudgetId,
                                                                                         @PageableDefault Pageable pageable,
                                                                                         PagedResourcesAssembler<ManualTimeItem> pagedResourcesAssembler,
                                                                                         ManualTimeItemResourceAssembler manualTimeItemResourceAssembler) {
        Page<ManualTimeItem> manualTimeItems = manualTimeItemService.getManualTimeItemsForBudgetWithId(timeBudgetId, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toResource(manualTimeItems, manualTimeItemResourceAssembler));
    }

    @DeleteMapping("/manual_item/{manualTimeItemId}")
    public HttpEntity<Void> deleteManualItem(@PathVariable("manualTimeItemId") long manualTimeItemId) {
        manualTimeItemService.removeManualTimeItemWithId(manualTimeItemId);
        return ResponseEntity.noContent().build();
    }
}
