package com.avoris.hotelsearch.application.dto;

import java.util.List;

public record SearchWithCount(String searchId,String hotelId,String checkIn,String checkOut,List<Integer>agesSorted,long count){}
