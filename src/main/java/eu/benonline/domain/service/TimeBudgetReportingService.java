package eu.benonline.domain.service;

import eu.benonline.domain.vo.TimeBudgetReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service layer class with methods to generate reports over the TimeBudget data.
 * Created by Benjamin Peter.
 */
@Service
public interface TimeBudgetReportingService {
    Page<TimeBudgetReport> getBalanceTill(LocalDate tillDate, Pageable pageable);
}
