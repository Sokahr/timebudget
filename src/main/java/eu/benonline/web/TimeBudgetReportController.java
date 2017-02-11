package eu.benonline.web;

import eu.benonline.domain.service.TimeBudgetReportingService;
import eu.benonline.domain.vo.TimeBudgetReport;
import eu.benonline.web.resources.TimeBudgetReportResourceAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Created by Benjamin Peter.
 */
@RestController
@RequestMapping("/time_budget/report")
@RequiredArgsConstructor
public class TimeBudgetReportController {

    private final TimeBudgetReportingService reportingService;

    @GetMapping
    public ResponseEntity getTimeBudgetBalances(
            @PageableDefault() Pageable pageable,
            PagedResourcesAssembler<TimeBudgetReport> pagedResourcesAssembler,
            TimeBudgetReportResourceAssembler timeBudgetReportResourceAssembler) {

        Page<TimeBudgetReport> reportPage = reportingService.getBalanceTill(LocalDate.now(), pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toResource(reportPage, timeBudgetReportResourceAssembler));
    }
}
