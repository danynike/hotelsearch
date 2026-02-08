package com.avoris.hotelsearch.application.dto;

import java.time.LocalDate;
import java.util.List;

public record CreateSearchCommand(String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages) {
}
