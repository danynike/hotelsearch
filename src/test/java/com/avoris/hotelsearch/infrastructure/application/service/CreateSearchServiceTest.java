package com.avoris.hotelsearch.infrastructure.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.avoris.hotelsearch.application.dto.CreateSearchCommand;
import com.avoris.hotelsearch.application.port.out.PublishSearchPort;
import com.avoris.hotelsearch.application.service.CreateSearchService;
import com.avoris.hotelsearch.domain.model.Search;

class CreateSearchServiceTest {

    @Test
    void create_publishesSearchAndReturnsId() {
        final PublishSearchPort publishPort = mock(PublishSearchPort.class);
        final CreateSearchService service = new CreateSearchService(publishPort);

        final var command = new CreateSearchCommand("1234aBc", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), List.of(30, 29, 1, 3));
        final var created = service.create(command);

        assertThat(created.searchId()).isNotBlank();

        final ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(publishPort).publish(captor.capture());

        final Search published = captor.getValue();
        assertThat(published.id().value()).isEqualTo(created.searchId());
        assertThat(published.hotelId().value()).isEqualTo("1234aBc");
        assertThat(published.normalizedKey().value()).isEqualTo("1234aBc|2023-12-29|2023-12-31|1,3,29,30");
    }
}
