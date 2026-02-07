package com.avoris.hotelsearch.domain.model;

import java.util.List;

public record Ages(List<Integer> values) {
    public Ages {
        if (values == null || values.isEmpty()) throw new IllegalArgumentException("ages is required");
        if (values.stream().anyMatch(v -> v == null || v < 0 || v > 120)) throw new IllegalArgumentException("invalid age");
        values = List.copyOf(values);
    }
}
