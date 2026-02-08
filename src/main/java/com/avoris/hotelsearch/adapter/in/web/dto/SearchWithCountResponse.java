package com.avoris.hotelsearch.adapter.in.web.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record SearchWithCountResponse(String searchId, SearchDto search, long count) {
    public record SearchDto(String hotelId, @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn, @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut,
            List<Integer> ages) {
    }
}