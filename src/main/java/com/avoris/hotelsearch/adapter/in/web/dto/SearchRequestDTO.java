package com.avoris.hotelsearch.adapter.in.web.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record SearchRequestDTO(@NotBlank String hotelId,@NotNull @Pattern(regexp="^\\d{2}/\\d{2}/\\d{4}$")String checkIn,@NotNull @Pattern(regexp="^\\d{2}/\\d{2}/\\d{4}$")String checkOut,@NotEmpty List<@NotNull @Min(0)@Max(120)Integer>ages){}
