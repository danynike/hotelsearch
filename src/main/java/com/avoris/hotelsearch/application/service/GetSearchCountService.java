package com.avoris.hotelsearch.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avoris.hotelsearch.application.dto.SearchWithCount;
import com.avoris.hotelsearch.application.port.in.GetSearchCountUseCase;
import com.avoris.hotelsearch.application.port.out.CountSimilarSearchesPort;
import com.avoris.hotelsearch.application.port.out.LoadSearchPort;
import com.avoris.hotelsearch.domain.exception.SearchNotFoundException;
import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.domain.model.SearchId;

@Service
public class GetSearchCountService implements GetSearchCountUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSearchCountService.class);

    private final LoadSearchPort loadSearchPort;
    private final CountSimilarSearchesPort countSimilarSearchesPort;

    public GetSearchCountService(final LoadSearchPort loadSearchPort, final CountSimilarSearchesPort countSimilarSearchesPort) {
        this.loadSearchPort = loadSearchPort;
        this.countSimilarSearchesPort = countSimilarSearchesPort;
    }

    @Override
    public SearchWithCount getById(final SearchId searchId) {
        final Search search = loadSearchPort.loadById(searchId).orElseThrow(() -> new SearchNotFoundException("Search not found for id " + searchId.value()));

        final long count = countSimilarSearchesPort.countByNormalizedKey(search.normalizedKey());
        final var sorted = search.ages().values().stream().sorted().toList();

        LOGGER.info("Count for searchId={} is {}", searchId.value(), count);

        return new SearchWithCount(search.id().value(), search.hotelId().value(), search.stayDates().checkIn(), search.stayDates().checkOut(), sorted, count);
    }
}
