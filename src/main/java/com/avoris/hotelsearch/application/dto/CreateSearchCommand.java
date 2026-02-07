package com.avoris.hotelsearch.application.dto;

import java.util.List;

public record CreateSearchCommand(String hotelId,String checkIn,String checkOut,List<Integer>ages){}
