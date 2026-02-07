package com.avoris.hotelsearch.domain.model;

public record NormalizedSearchKey(String value) {
    public NormalizedSearchKey {
        if (value == null) throw new IllegalArgumentException("normalized key is required");
    }
}
