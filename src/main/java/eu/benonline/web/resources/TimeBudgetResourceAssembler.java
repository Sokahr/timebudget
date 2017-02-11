package eu.benonline.web.resources;

import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.web.TimeBudgetController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class TimeBudgetResourceAssembler extends ResourceAssemblerSupport<TimeBudget, TimeBudgetResource> {

    /**
     * Creates a new {@link ResourceAssemblerSupport} using {@link TimeBudgetController} and {@link TimeBudgetResource}.
     */
    public TimeBudgetResourceAssembler() {
        super(TimeBudgetController.class, TimeBudgetResource.class);
    }

    /**
     * Converts the given entity into an {@link TimeBudgetResource}.
     *
     * @param entity {@link TimeBudget}
     * @return returns {@link TimeBudgetResource}
     */
    @Override
    public TimeBudgetResource toResource(TimeBudget entity) {
        return createResourceWithId(entity.getId(), entity);
    }
}
