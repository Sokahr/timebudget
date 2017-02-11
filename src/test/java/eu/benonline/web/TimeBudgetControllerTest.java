package eu.benonline.web;

import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.service.TimeBudgetService;
import eu.benonline.web.resources.TimeBudgetResource;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Benjamin Peter.
 */
@WebMvcTest(TimeBudgetController.class)
public class TimeBudgetControllerTest extends AbstractRestControllerTest {

    @SuppressWarnings("CanBeFinal")
    @MockBean
    private TimeBudgetService timeBudgetService;

    @Test
    public void createTimeBudget() throws Exception {
        given(this.timeBudgetService.createNewTimeBudgetWithName("new budget"))
                .willReturn(new TimeBudget("new budget"));

        TimeBudgetResource newTimeBudget = new TimeBudgetResource(0, "new budget");
        newTimeBudget.setBudgetName("new budget");

        mockMvc.perform(
                post("/time_budget")
                        .contentType(MediaType.APPLICATION_JSON).content(
                        "{\"budgetName\":\"new budget\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteTimeBudget() throws Exception {
        mockMvc.perform(delete("/time_budget/42")).andExpect(status().isNoContent());
    }

    @Test
    public void updateTimeBudget_resourceNotFound() throws Exception {
        TimeBudgetResource timeBudgetResource = new TimeBudgetResource(42, "testChange");
        given(timeBudgetService.updateTimeBudgetNameForBudgetWithId("testChange", 42))
                .willReturn(null);

        mockMvc.perform(put("/time_budget/42").content(toJsonString(timeBudgetResource)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateTimeBudget_accepted() throws Exception {
        TimeBudgetResource timeBudgetResource = new TimeBudgetResource(42, "testChange");
        TimeBudget timeBudget = new TimeBudget("testChanged");
        given(timeBudgetService.updateTimeBudgetNameForBudgetWithId("testChange", 42))
                .willReturn(timeBudget);

        mockMvc.perform(put("/time_budget/42").content(toJsonString(timeBudgetResource)).contentType(MediaType
                .APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }
}