package com.avoris.hotelsearch.infrastructure.adapter.in.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.avoris.hotelsearch.application.port.in.CreateSearchUseCase;
import com.avoris.hotelsearch.application.port.in.GetSearchCountUseCase;

@WebMvcTest(controllers = {SearchController.class, GlobalExceptionHandler.class })
class SearchControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateSearchUseCase createSearchUseCase;

    @MockitoBean
    private GetSearchCountUseCase getSearchCountUseCase;

    @Test
    void createSearch_returns400_whenInvalidPayload() throws Exception {
        final String payload = """
                {
                  "hotelId": "",
                  "checkIn": "29/12/2023",
                  "checkOut": null,
                  "ages": []
                }
                """;

        final var mvcResult = mockMvc.perform(post("/api/1/search").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed")).andReturn();

        final String body = mvcResult.getResponse().getContentAsString();

        org.junit.jupiter.api.Assertions.assertAll(() -> Assertions.assertThat(body).contains("hotelId"),
                () -> Assertions.assertThat(body).contains("checkOut"), () -> Assertions.assertThat(body).contains("ages"),
                () -> Assertions.assertThat(body).doesNotContain("checkIn"));
    }

    @Test
    void createSearch_returns400_whenDateFormatIsInvalid() throws Exception {
        final String payload = """
                {
                  "hotelId": "1234aBc",
                  "checkIn": "2023-12-29",
                  "checkOut": "31/12/2023",
                  "ages": [30]
                }
                """;

        mockMvc.perform(post("/api/1/search").contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON or invalid field format"));
    }

}
