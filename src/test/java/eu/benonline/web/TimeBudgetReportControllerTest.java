package eu.benonline.web;

import eu.benonline.domain.service.TimeBudgetReportingService;
import eu.benonline.domain.vo.TimeBudgetReport;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Benjamin Peter.
 */
@EnableSpringDataWebSupport
@WebMvcTest(TimeBudgetReportController.class)
public class TimeBudgetReportControllerTest extends AbstractRestControllerTest {

    @SuppressWarnings("CanBeFinal")
    @MockBean
    private TimeBudgetReportingService timeBudgetReportingService;

    @Test
    public void getTimeBudgetBalance() throws Exception {

        List<TimeBudgetReport> fakeReports = new ArrayList<>();
        fakeReports.add(new TimeBudgetReport("test", -1));
        fakeReports.add(new TimeBudgetReport("test2", 0));
        fakeReports.add(new TimeBudgetReport("test3", 8));
        Page<TimeBudgetReport> fakePage = new PageImpl<>(fakeReports.subList(1, 2), new PageRequest(1,
                1), 3);
        when(timeBudgetReportingService.getBalanceTill(any(), any())).thenReturn(fakePage);

        mockMvc.perform(get("/time_budget/report").param("page", "1").param("size", "1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json("{\"_embedded\":" +
                        "{\"timeBudgetReports\":[" +
                        "{\"budgetName\":\"test2\",\"balance\":0.0}]},\"" +
                        "_links\":" +
                        "{\"first\":{\"href\":\"http://localhost/time_budget/report?page=0&size=1\"}," +
                        "\"prev\":{\"href\":\"http://localhost/time_budget/report?page=0&size=1\"}," +
                        "\"self\":{\"href\":\"http://localhost/time_budget/report\"}," +
                        "\"next\":{\"href\":\"http://localhost/time_budget/report?page=2&size=1\"}," +
                        "\"last\":{\"href\":\"http://localhost/time_budget/report?page=2&size=1\"}}," +
                        "\"page\":{\"size\":1,\"totalElements\":3,\"totalPages\":3,\"number\":1}}"));
    }
}
