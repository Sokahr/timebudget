package eu.benonline.web.resources;

import eu.benonline.domain.entity.ManualTimeItem;
import eu.benonline.web.TimeBudgetManualItemController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * Created by Benjamin Peter.
 */
public class ManualTimeItemResourceAssembler extends ResourceAssemblerSupport<ManualTimeItem, ManualTimeItemResource> {

    /**
     * Creates a new {@link ResourceAssemblerSupport} using {@link TimeBudgetManualItemController} and
     * {@link ManualTimeItemResource}.
     */
    public ManualTimeItemResourceAssembler() {
        super(TimeBudgetManualItemController.class, ManualTimeItemResource.class);
    }

    /**
     * Converts the given entity into an {@link ManualTimeItemResource}.
     *
     * @param entity {@link ManualTimeItem}
     * @return returns {@link ManualTimeItemResource}
     */
    @Override
    public ManualTimeItemResource toResource(ManualTimeItem entity) {
        ManualTimeItemResource manualTimeItemResource = new ManualTimeItemResource(

        );
        manualTimeItemResource.timeType = entity.getTimeType();
        manualTimeItemResource.tillDate = entity.getDateRange().getTillDate();
        manualTimeItemResource.fromDate = entity.getDateRange().getFromDate();
        manualTimeItemResource.hours = entity.getHours().getValue();
        return manualTimeItemResource;
    }
}
