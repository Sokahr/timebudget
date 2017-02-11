package eu.benonline.domain.service;

import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.entity.TimeBudgetRepository;
import eu.benonline.domain.vo.TimeBudgetReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin Peter.
 */
@RequiredArgsConstructor
public class TimeBudgetReportingServiceImpl implements TimeBudgetReportingService {

    private final TimeBudgetRepository timeBudgetRepository;

    @Override
    public Page<TimeBudgetReport> getBalanceTill(LocalDate tillDate, Pageable pageable) {
        Page<TimeBudget> timeBudgets = timeBudgetRepository.findAll(pageable);
        List<TimeBudgetReport> reports = new ArrayList<>();
        for (TimeBudget timeBudget :
                timeBudgets) {
            TimeBudgetReport report = new TimeBudgetReport(timeBudget.getName(),
                    timeBudget.getTimeBalanceTill(tillDate));
            reports.add(report);
        }
        return new PageImpl<>(reports, pageable, timeBudgets.getTotalElements());
    }
}
