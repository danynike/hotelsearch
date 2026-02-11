package com.avoris.hotelsearch.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AgesTest {

    @Test
    void ages_isDefensiveCopy_andImmutableView() {
        final List<Integer> input = new ArrayList<>(List.of(10, 20));
        final Ages ages = new Ages(input);

        input.add(30);

        Assertions.assertAll(() -> assertThat(ages.values()).containsExactly(10, 20),
                () -> assertThatThrownBy(() -> ages.values().add(99)).isInstanceOf(UnsupportedOperationException.class));
    }
}
