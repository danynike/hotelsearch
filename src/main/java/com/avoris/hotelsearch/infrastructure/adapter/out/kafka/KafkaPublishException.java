package com.avoris.hotelsearch.infrastructure.adapter.out.kafka;

public class KafkaPublishException extends RuntimeException {
    private static final long serialVersionUID = 7344140895382690605L;

    public KafkaPublishException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
