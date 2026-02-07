package com.avoris.hotelsearch.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import com.avoris.hotelsearch.domain.model.Ages;
import com.avoris.hotelsearch.domain.model.HotelId;
import com.avoris.hotelsearch.domain.model.NormalizedSearchKey;
import com.avoris.hotelsearch.domain.model.StayDates;

public final class SearchKeyNormalizer {

    public static final String FIELD_DELIMITER = "|";
    public static final String AGES_DELIMITER = ",";

    private SearchKeyNormalizer() {
    }

    public static NormalizedSearchKey normalize(final HotelId hotelId, final StayDates stayDates, final Ages ages) {
        final List<Integer> sorted = ages.values().stream().sorted().toList();
        final String agesPart = sorted.stream().map(String::valueOf).collect(Collectors.joining(AGES_DELIMITER));
        final String key = hotelId.value() + FIELD_DELIMITER + stayDates.checkIn() + FIELD_DELIMITER + stayDates.checkOut() + FIELD_DELIMITER + agesPart;
        return new NormalizedSearchKey(key);
    }
}
