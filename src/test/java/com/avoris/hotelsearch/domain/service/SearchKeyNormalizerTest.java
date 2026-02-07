package com.avoris.hotelsearch.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.avoris.hotelsearch.domain.model.Ages;
import com.avoris.hotelsearch.domain.model.HotelId;
import com.avoris.hotelsearch.domain.model.StayDates;

class SearchKeyNormalizerTest {

    @Test
    void normalize_buildsExpectedKeySortingAges() {
        final var hotelId = new HotelId("1234aBc");
        final var dates = new StayDates("29/12/2023", "31/12/2023");
        final var ages = new Ages(List.of(30, 29, 1, 3));

        final var key = SearchKeyNormalizer.normalize(hotelId, dates, ages);

        assertThat(key.value()).isEqualTo("1234aBc|29/12/2023|31/12/2023|1,3,29,30");
    }

    @Test
    void normalize_throwsWhenHotelIdInvalid() {
        final var dates = new StayDates("29/12/2023", "31/12/2023");
        final var ages = new Ages(List.of(1));

        assertThatThrownBy(() -> SearchKeyNormalizer.normalize(new HotelId(""), dates, ages)).isInstanceOf(IllegalArgumentException.class);
    }
}
