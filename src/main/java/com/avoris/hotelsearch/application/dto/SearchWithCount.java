package com.avoris.hotelsearch.application.dto;

import java.time.LocalDate;
import java.util.List;

public record SearchWithCount(String searchId, String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> agesSorted, long count) {
}
