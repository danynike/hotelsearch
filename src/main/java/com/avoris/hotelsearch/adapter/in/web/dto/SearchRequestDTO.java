package com.avoris.hotelsearch.adapter.in.web.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SearchRequestDTO(@NotBlank String hotelId, @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut, @NotEmpty List<@NotNull @Min(0) @Max(120) Integer> ages) {
}
