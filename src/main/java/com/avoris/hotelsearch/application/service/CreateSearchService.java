package com.avoris.hotelsearch.application.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avoris.hotelsearch.application.dto.CreateSearchCommand;
import com.avoris.hotelsearch.application.dto.CreatedSearch;
import com.avoris.hotelsearch.application.port.in.CreateSearchUseCase;
import com.avoris.hotelsearch.application.port.out.PublishSearchPort;
import com.avoris.hotelsearch.domain.model.Ages;
import com.avoris.hotelsearch.domain.model.HotelId;
import com.avoris.hotelsearch.domain.model.NormalizedSearchKey;
import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.domain.model.SearchId;
import com.avoris.hotelsearch.domain.model.StayDates;
import com.avoris.hotelsearch.domain.service.SearchKeyNormalizer;

@Service
public class CreateSearchService implements CreateSearchUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSearchService.class);

    private final PublishSearchPort publishSearchPort;

    public CreateSearchService(final PublishSearchPort publishSearchPort) {
        this.publishSearchPort = publishSearchPort;
    }

    @Override
    public CreatedSearch create(final CreateSearchCommand command) {
        final SearchId id = new SearchId(UUID.randomUUID().toString());
        final HotelId hotelId = new HotelId(command.hotelId());
        final StayDates dates = new StayDates(command.checkIn(), command.checkOut());
        final Ages ages = new Ages(command.ages());
        final NormalizedSearchKey key = SearchKeyNormalizer.normalize(hotelId, dates, ages);

        final Search search = new Search(id, hotelId, dates, ages, key);
        LOGGER.info("Creating search id={} hotelId={}", id.value(), hotelId.value());
        publishSearchPort.publish(search);

        return new CreatedSearch(id.value());
    }
}
