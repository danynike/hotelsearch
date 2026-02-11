package com.avoris.hotelsearch.infrastructure.adapter.out.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.avoris.hotelsearch.application.port.out.PublishSearchPort;
import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.infrastructure.adapter.out.kafka.dto.SearchMessage;

@Component
public class SearchKafkaProducerAdapter implements PublishSearchPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchKafkaProducerAdapter.class);

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

        kafkaTemplate.send(topic, search.id().value(), message).whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.error("Kafka publish failed topic={} key={}", topic, search.id().value(), ex);
                throw new KafkaPublishException("Kafka publish failed for searchId " + search.id().value(), ex);
            }
            LOGGER.info("Kafka publish success topic={} key={} partition={} offset={}", topic, search.id().value(), result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        }).join();
    }
}
