package com.avoris.hotelsearch.domain.model;

public record HotelId(String value) {
    public HotelId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("HotelId is required");
    }
}
