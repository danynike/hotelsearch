package com.avoris.hotelsearch.infrastructure.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import com.avoris.hotelsearch.infrastructure.adapter.out.kafka.dto.SearchMessage;

@Configuration
public class KafkaConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${hotel.kafka.topic}")
    private String topicName;

    @Value("${hotel.kafka.partitions:3}")
    private int partitions;

    @Value("${hotel.kafka.replicas:1}")
    private short replicas;

    @Value("${hotel.kafka.listener.concurrency:3}")
    private int concurrency;

    @Bean
    NewTopic hotelSearchTopic() {
        return TopicBuilder.name(topicName).partitions(partitions).replicas(replicas).build();
    }

    @Bean
    ProducerFactory<String, SearchMessage> producerFactory() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    KafkaTemplate<String, SearchMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    ConsumerFactory<String, SearchMessage>
                   consumerFactory(@Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}") final String trustedPackages) {
        final JsonDeserializer<SearchMessage> deserializer = new JsonDeserializer<>(SearchMessage.class, false);
        deserializer.addTrustedPackages(trustedPackages);

        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    DefaultErrorHandler kafkaErrorHandler() {
        final FixedBackOff backOff = new FixedBackOff(1000L, 3L);
        final DefaultErrorHandler handler = new DefaultErrorHandler((record, ex) -> {
            LOGGER.error("Kafka listener failed topic={} partition={} offset={} key={}", record.topic(), record.partition(), record.offset(), record.key(), ex);
        }, backOff);
        handler.setRetryListeners((record, ex, deliveryAttempt) -> LOGGER.warn("Kafka retry attempt={} topic={} partition={} offset={}", deliveryAttempt,
                record.topic(), record.partition(), record.offset(), ex));
        return handler;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, SearchMessage> kafkaListenerContainerFactory(final ConsumerFactory<String, SearchMessage> consumerFactory,
            final DefaultErrorHandler kafkaErrorHandler) {
        final ConcurrentKafkaListenerContainerFactory<String, SearchMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        factory.setConcurrency(concurrency);
        return factory;
    }
}
