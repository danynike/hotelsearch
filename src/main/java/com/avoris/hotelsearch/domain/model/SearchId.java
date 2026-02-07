package com.avoris.hotelsearch.domain.model;

public record SearchId(String value) {
    public SearchId {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("SearchId is required");
    }
}
