package com.avoris.hotelsearch.adapter.in.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.avoris.hotelsearch.adapter.out.kafka.dto.SearchMessage;
import com.avoris.hotelsearch.application.port.in.PersistSearchUseCase;
import com.avoris.hotelsearch.domain.model.Search;

class SearchKafkaConsumerTest {

    @Test
    void consume_mapsMessageToDomainAndCallsUseCase() {
        final PersistSearchUseCase useCase = mock(PersistSearchUseCase.class);
        final SearchKafkaConsumer consumer = new SearchKafkaConsumer(useCase);

        final SearchMessage message = new SearchMessage("search-id", "1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29, 1, 3));

        consumer.consume(message);

        final ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(useCase).persist(captor.capture());

        final Search search = captor.getValue();
        assertThat(search.id().value()).isEqualTo("search-id");
        assertThat(search.normalizedKey().value()).isEqualTo("1234aBc|2023-12-29|2023-12-31|1,3,29,30");
    }
}
