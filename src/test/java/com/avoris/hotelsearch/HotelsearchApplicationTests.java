package com.avoris.hotelsearch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.avoris.hotelsearch.infrastructure.adapter.in.web.dto.SearchCreatedResponse;
import com.avoris.hotelsearch.infrastructure.adapter.out.mongo.SpringDataSearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfSystemProperty(named = "it.external", matches = "true")
class SearchFlowIntegrationExternalDockerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataSearchRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void fullFlow_twoSimilarSearches_countIsTwoAndAgesSorted() throws Exception {
        final String payload1 = """
                {
                  "hotelId": "1234aBc",
                  "checkIn": "29/12/2023",
                  "checkOut": "31/12/2023",
                  "ages": [30, 29, 1, 3]
                }
                """;

        final String payload2 = """
                {
                  "hotelId": "1234aBc",
                  "checkIn": "29/12/2023",
                  "checkOut": "31/12/2023",
                  "ages": [3, 30, 1, 29]
                }
                """;

        final String response1 = mockMvc.perform(post("/api/1/search").contentType(MediaType.APPLICATION_JSON).content(payload1)).andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").exists()).andReturn().getResponse().getContentAsString();

        final SearchCreatedResponse created1 = objectMapper.readValue(response1, SearchCreatedResponse.class);

        final String response2 = mockMvc.perform(post("/api/1/search").contentType(MediaType.APPLICATION_JSON).content(payload2)).andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").exists()).andReturn().getResponse().getContentAsString();

        final SearchCreatedResponse created2 = objectMapper.readValue(response2, SearchCreatedResponse.class);

        assertThat(created1.searchId()).isNotBlank();
        assertThat(created2.searchId()).isNotBlank();

        Awaitility.await().atMost(Duration.ofSeconds(30)).until(() -> repository.count() == 2);

        mockMvc.perform(get("/api/1/count").param("searchId", created2.searchId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId", is(created2.searchId()))).andExpect(jsonPath("$.count", is(2)))
                .andExpect(jsonPath("$.search.ages", hasSize(4))).andExpect(jsonPath("$.search.checkIn", is("29/12/2023")))
                .andExpect(jsonPath("$.search.checkOut", is("31/12/2023")));
    }
}
