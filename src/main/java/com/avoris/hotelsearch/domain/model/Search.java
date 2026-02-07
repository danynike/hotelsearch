package com.avoris.hotelsearch.domain.model;

public record Search(SearchId id, HotelId hotelId, StayDates stayDates, Ages ages, NormalizedSearchKey normalizedKey) {
}
