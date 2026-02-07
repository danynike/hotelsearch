package com.avoris.hotelsearch.domain.model;

public record StayDates(String checkIn, String checkOut) {
    public StayDates {
        if (checkIn == null || checkIn.isBlank()) throw new IllegalArgumentException("checkIn is required");
        if (checkOut == null || checkOut.isBlank()) throw new IllegalArgumentException("checkOut is required");
    }
}
