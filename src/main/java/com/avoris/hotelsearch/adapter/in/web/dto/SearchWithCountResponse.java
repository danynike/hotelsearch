package com.avoris.hotelsearch.adapter.in.web.dto;

import java.util.List;

public record SearchWithCountResponse(String searchId,SearchDto search,long count){public record SearchDto(String hotelId,String checkIn,String checkOut,List<Integer>ages){}}
