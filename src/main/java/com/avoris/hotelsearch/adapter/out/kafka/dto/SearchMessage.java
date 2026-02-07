package com.avoris.hotelsearch.adapter.out.kafka.dto;

import java.util.List;

public record SearchMessage(String searchId,String hotelId,String checkIn,String checkOut,List<Integer>ages){}
