package eu.benonline.web.resources;

import eu.benonline.domain.vo.TimeBudgetReport;
import eu.benonline.web.TimeBudgetReportController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by Benjamin Peter.
 */
@Component

public class TimeBudgetReportResourceAssembler extends ResourceAssemblerSupport<TimeBudgetReport, TimeBudgetReportResource> {
    /**
     * Creates a new {@link ResourceAssemblerSupport} using {@link TimeBudgetReportController} and {@link TimeBudgetReportResource}.
     */
    public TimeBudgetReportResourceAssembler() {
        super(TimeBudgetReportController.class, TimeBudgetReportResource.class);
    }

    /**
     * Converts the given entity into an {@link TimeBudgetReportResource}.
     *
     * @param entity {@link TimeBudgetReport}
     * @return returns {@link TimeBudgetReportResource}
     */
    @Override
    public TimeBudgetReportResource toResource(TimeBudgetReport entity) {
        TimeBudgetReportResource result = new TimeBudgetReportResource();
        result.balance = entity.getBalance();
        result.budgetName = entity.getBudgetName();
        return result;
    }
}
