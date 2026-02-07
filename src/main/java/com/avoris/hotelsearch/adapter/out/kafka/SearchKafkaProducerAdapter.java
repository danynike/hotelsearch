package com.avoris.hotelsearch.adapter.out.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.avoris.hotelsearch.adapter.out.kafka.dto.SearchMessage;
import com.avoris.hotelsearch.application.port.out.PublishSearchPort;
import com.avoris.hotelsearch.domain.model.Search;

@Component
public class SearchKafkaProducerAdapter implements PublishSearchPort {

    private final KafkaTemplate<String, SearchMessage> kafkaTemplate;
    private final String topic;

    public SearchKafkaProducerAdapter(final KafkaTemplate<String, SearchMessage> kafkaTemplate, final @Value("${hotel.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(final Search search) {
        final SearchMessage message = new SearchMessage(search.id().value(), search.hotelId().value(), search.stayDates().checkIn(),
                search.stayDates().checkOut(), search.ages().values());
        kafkaTemplate.send(topic, search.id().value(), message);
    }
}
