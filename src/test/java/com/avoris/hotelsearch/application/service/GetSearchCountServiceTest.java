package com.avoris.hotelsearch.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.avoris.hotelsearch.application.port.out.CountSimilarSearchesPort;
import com.avoris.hotelsearch.application.port.out.LoadSearchPort;
import com.avoris.hotelsearch.domain.exception.SearchNotFoundException;
import com.avoris.hotelsearch.domain.model.Ages;
import com.avoris.hotelsearch.domain.model.HotelId;
import com.avoris.hotelsearch.domain.model.NormalizedSearchKey;
import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.domain.model.SearchId;
import com.avoris.hotelsearch.domain.model.StayDates;

class GetSearchCountServiceTest {

    @Test
    void getById_returnsSortedAgesAndCount() {
        final LoadSearchPort loadPort = mock(LoadSearchPort.class);
        final CountSimilarSearchesPort countPort = mock(CountSimilarSearchesPort.class);
        final GetSearchCountService service = new GetSearchCountService(loadPort, countPort);

        final SearchId id = new SearchId("search-id");
        final var search = new Search(id, new HotelId("1234aBc"), new StayDates(LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31)),
                new Ages(List.of(30, 29, 1, 3)), new NormalizedSearchKey("1234aBc|29/12/2023|31/12/2023|1,3,29,30"));

        when(loadPort.loadById(id)).thenReturn(Optional.of(search));
        when(countPort.countByNormalizedKey(search.normalizedKey())).thenReturn(2L);

        final var result = service.getById(id);

        assertThat(result.searchId()).isEqualTo("search-id");
        assertThat(result.agesSorted()).containsExactly(1, 3, 29, 30);
        assertThat(result.count()).isEqualTo(2L);
        assertThat(result.checkIn()).isEqualTo(LocalDate.of(2023, 12, 29));
        assertThat(result.checkOut()).isEqualTo(LocalDate.of(2023, 12, 31));
    }

    @Test
    void getById_throwsWhenNotFound() {
        final LoadSearchPort loadPort = mock(LoadSearchPort.class);
        final CountSimilarSearchesPort countPort = mock(CountSimilarSearchesPort.class);
        final GetSearchCountService service = new GetSearchCountService(loadPort, countPort);

        final SearchId id = new SearchId("missing");
        when(loadPort.loadById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id)).isInstanceOf(SearchNotFoundException.class);
    }
}
