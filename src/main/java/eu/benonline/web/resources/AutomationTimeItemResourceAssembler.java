package eu.benonline.web.resources;

import eu.benonline.domain.entity.AutomationTimeItem;
import eu.benonline.web.TimeBudgetAutomationItemController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by Benjamin Peter.
 */
@Component
public class AutomationTimeItemResourceAssembler extends ResourceAssemblerSupport<AutomationTimeItem, AutomationTimeItemResource> {
    /**
     * Creates a new {@link ResourceAssemblerSupport} using {@link TimeBudgetAutomationItemController} and {@link AutomationTimeItemResource}.
     */
    public AutomationTimeItemResourceAssembler() {
        super(TimeBudgetAutomationItemController.class, AutomationTimeItemResource.class);
    }


    /**
     * Converts the given entity into an {@link AutomationTimeItemResource}.
     *
     * @param entity {@link AutomationTimeItem}
     * @return the converted {@link AutomationTimeItemResource}
     */
    @Override
    public AutomationTimeItemResource toResource(AutomationTimeItem entity) {
        return new AutomationTimeItemResource(entity.getHours().getValue(),
                entity.getInterval(),
                entity.getOpenDateRange().getBeginDate(),
                entity.getOpenDateRange().getTillDate());
    }
}
