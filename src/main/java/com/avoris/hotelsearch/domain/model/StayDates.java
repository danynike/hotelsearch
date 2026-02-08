package com.avoris.hotelsearch.domain.model;

import java.time.LocalDate;

public record StayDates(LocalDate checkIn, LocalDate checkOut) {
    public StayDates {
        if (checkIn == null) throw new IllegalArgumentException("checkIn is required");
        if (checkOut == null) throw new IllegalArgumentException("checkOut is required");
        if (checkOut.isBefore(checkIn)) throw new IllegalArgumentException("checkOut must be after checkIn");
    }
}
