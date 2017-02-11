package eu.benonline.web;

import eu.benonline.domain.entity.AutomationTimeItem;
import eu.benonline.domain.entity.TimeBudget;
import eu.benonline.domain.service.TimeBudgetAutomationTimeItemService;
import eu.benonline.domain.vo.AutomationInterval;
import eu.benonline.domain.vo.OpenDateRange;
import eu.benonline.domain.vo.WorkingHours;
import eu.benonline.web.resources.AutomationTimeItemResource;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Benjamin Peter.
 */
@WebMvcTest(TimeBudgetAutomationItemController.class)
@EnableSpringDataWebSupport
public class TimeBudgetAutomationItemControllerTest extends AbstractRestControllerTest {

    @SuppressWarnings("CanBeFinal")
    @MockBean
    private TimeBudgetAutomationTimeItemService timeBudgetAutomationTimeItemServiceMock;

    @Test
    public void addNewAutomationTimeItem() throws Exception {
        AutomationTimeItemResource automationTimeItemResource = new AutomationTimeItemResource(8,
                AutomationInterval.DAILY,
                LocalDate.of(2017, 1, 1),
                null);
        TimeBudget fakedTimeBudget = new TimeBudget("fake");
        fakedTimeBudget.setId(42);
        AutomationTimeItem fakedAutomationTimeItem = new AutomationTimeItem(new WorkingHours(8),
                AutomationInterval.DAILY,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null));
        fakedAutomationTimeItem.setId(1);
        fakedAutomationTimeItem.setParentBudget(fakedTimeBudget);

        given(timeBudgetAutomationTimeItemServiceMock.addNewAutomationTimeItemToBudgetWithId(any(), eq(42L)))
                .willReturn(fakedAutomationTimeItem);

        String jsonString = toJsonString(automationTimeItemResource);
        mockMvc.perform(post("/time_budget/42/automation").content(jsonString)
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/time_budget/42/automation/1"));
    }

    @Test
    public void getAllAutomationTimeItemsForABudgetPaged() throws Exception {

        List<AutomationTimeItem> content = new ArrayList<>();
        content.add(new AutomationTimeItem(new WorkingHours(8),
                AutomationInterval.MONDAYS,
                new OpenDateRange(LocalDate.of(2017, 1, 1), null)));
        Page<AutomationTimeItem> fakePage = new PageImpl<>(content);

        given(timeBudgetAutomationTimeItemServiceMock.getAutomationTimeItemsForBudgetWithId(eq(42L), any()))
                .willReturn(fakePage);

        mockMvc.perform(get("/time_budget/42/automations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..automationTimeItems.length()", contains(1)));

        verify(timeBudgetAutomationTimeItemServiceMock, times(1))
                .getAutomationTimeItemsForBudgetWithId(eq(42L), any());
    }

    @Test
    public void deleteAutomationTimeItem() throws Exception {
        mockMvc.perform(delete("/time_budget/42/automation/23"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(timeBudgetAutomationTimeItemServiceMock, times(1))
                .removeAutomationTimeItemWithId(23);
    }
}