package com.avoris.hotelsearch.infrastructure.adapter.in.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.avoris.hotelsearch.application.port.in.PersistSearchUseCase;
import com.avoris.hotelsearch.domain.model.Ages;
import com.avoris.hotelsearch.domain.model.HotelId;
import com.avoris.hotelsearch.domain.model.NormalizedSearchKey;
import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.domain.model.SearchId;
import com.avoris.hotelsearch.domain.model.StayDates;
import com.avoris.hotelsearch.domain.service.SearchKeyNormalizer;
import com.avoris.hotelsearch.infrastructure.adapter.out.kafka.dto.SearchMessage;

@Component
public class SearchKafkaConsumer {

    private final PersistSearchUseCase persistSearchUseCase;

    public SearchKafkaConsumer(final PersistSearchUseCase persistSearchUseCase) {
        this.persistSearchUseCase = persistSearchUseCase;
    }

    @KafkaListener(topics = "${hotel.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(final SearchMessage message) {
        final SearchId id = new SearchId(message.searchId());
        final HotelId hotelId = new HotelId(message.hotelId());
        final StayDates dates = new StayDates(message.checkIn(), message.checkOut());
        final Ages ages = new Ages(message.ages());
        final NormalizedSearchKey key = SearchKeyNormalizer.normalize(hotelId, dates, ages);
        persistSearchUseCase.persist(new Search(id, hotelId, dates, ages, key));
    }
}
