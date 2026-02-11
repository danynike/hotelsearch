package com.avoris.hotelsearch.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.avoris.hotelsearch.application.dto.CreatedSearch;
import com.avoris.hotelsearch.application.dto.SearchWithCount;
import com.avoris.hotelsearch.application.port.in.CreateSearchUseCase;
import com.avoris.hotelsearch.application.port.in.GetSearchCountUseCase;
import com.avoris.hotelsearch.domain.model.SearchId;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {SearchController.class, GlobalExceptionHandler.class })
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateSearchUseCase createSearchUseCase;

    @MockitoBean
    private GetSearchCountUseCase getSearchCountUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createSearch_returnsSearchId() throws Exception {
        when(createSearchUseCase.create(any())).thenReturn(new CreatedSearch("abc-123"));

        final String payload = """
                {
                  "hotelId": "1234aBc",
                  "checkIn": "29/12/2023",
                  "checkOut": "31/12/2023",
                  "ages": [30, 29, 1, 3]
                }
                """;

        mockMvc.perform(post("/api/1/search").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("abc-123"));
    }

    @Test
    void count_returnsSearchWithCount() throws Exception {
        when(getSearchCountUseCase.getById(any(SearchId.class)))
                .thenReturn(new SearchWithCount("abc-123", "1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(1, 3, 29, 30), 2L));

        mockMvc.perform(get("/api/1/count").param("searchId", "abc-123")).andExpect(status().isOk()).andExpect(jsonPath("$.searchId").value("abc-123"))
                .andExpect(jsonPath("$.count").value(2)).andExpect(jsonPath("$.search.ages[0]").value(1))
                .andExpect(jsonPath("$.search.checkIn").value("29/12/2023")).andExpect(jsonPath("$.search.checkOut").value("31/12/2023"));
    }
}
