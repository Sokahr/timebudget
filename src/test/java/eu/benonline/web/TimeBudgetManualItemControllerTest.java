package eu.benonline.web;

import eu.benonline.domain.entity.ManualTimeItem;
import eu.benonline.domain.service.TimeBudgetManualTimeItemService;
import eu.benonline.domain.vo.DateRange;
import eu.benonline.domain.vo.TimeType;
import eu.benonline.domain.vo.WorkingHours;
import eu.benonline.web.resources.ManualTimeItemResource;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Benjamin Peter.
 */

@WebMvcTest(TimeBudgetManualItemController.class)
@EnableSpringDataWebSupport
public class TimeBudgetManualItemControllerTest extends AbstractRestControllerTest {

    @SuppressWarnings("CanBeFinal")
    @MockBean
    private TimeBudgetManualTimeItemService manualTimeItemService;

    @Test
    public void addNewManualTimeItemToBudget() throws Exception {
        ManualTimeItemResource manualTimeItemResource = new ManualTimeItemResource();
        manualTimeItemResource.setHours(1);
        manualTimeItemResource.setFromDate(LocalDate.of(2017, 1, 1));
        manualTimeItemResource.setTillDate(LocalDate.of(2017, 1, 1));
        manualTimeItemResource.setTimeType(TimeType.DONE);

        ManualTimeItem fakeCreatedItem = new ManualTimeItem(new WorkingHours(1),
                new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)), TimeType.DONE);
        fakeCreatedItem.setId(1);

        when(manualTimeItemService.addManualTimeItemToBudgetWithID(any(), eq(42L)))
                .thenReturn(fakeCreatedItem);

        mockMvc.perform(post("/time_budget/42/manual_item")
                .content(toJsonString(manualTimeItemResource)).contentType(contentType))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/time_budget/42/manual_item/1"));

        ArgumentCaptor<ManualTimeItem> manualTimeItemArgumentCaptor = ArgumentCaptor.forClass(ManualTimeItem.class);
        verify(manualTimeItemService, times(1))
                .addManualTimeItemToBudgetWithID(manualTimeItemArgumentCaptor.capture(), eq(42L));

        ManualTimeItem capturedValue = manualTimeItemArgumentCaptor.getValue();

        assertThat(capturedValue.getDateRange().getFromDate()).isEqualTo(manualTimeItemResource.getFromDate());
        assertThat(capturedValue.getDateRange().getTillDate()).isEqualTo(manualTimeItemResource.getTillDate());
        assertThat(capturedValue.getHours().getValue()).isEqualTo(manualTimeItemResource.getHours());
        assertThat(capturedValue.getTimeType()).isEqualToComparingOnlyGivenFields(manualTimeItemResource.getTimeType());
    }

    @Test
    public void getAllManualTimeItemsForBudget() throws Exception {

        List<ManualTimeItem> manualTimeItems = new ArrayList<>();
        manualTimeItems.add(
                new ManualTimeItem(
                        new WorkingHours(8),
                        new DateRange(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 1, 1)),
                        TimeType.DONE));
        Page<ManualTimeItem> fakePage = new PageImpl<>(manualTimeItems);
        when(manualTimeItemService.getManualTimeItemsForBudgetWithId(eq(42L), any()))
                .thenReturn(fakePage);

        mockMvc.perform(get("/time_budget/42/manual_items"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$..manualTimeItems.length()", org.hamcrest.Matchers.contains(1)));

        verify(manualTimeItemService, times(1)).getManualTimeItemsForBudgetWithId(eq(42L), any());
    }

    @Test
    public void deleteManualTimeItem() throws Exception {

        mockMvc.perform(delete("/time_budget/42/manual_item/23"))
                .andExpect(status().isNoContent());

        verify(manualTimeItemService, times(1)).removeManualTimeItemWithId(23L);
    }
}
