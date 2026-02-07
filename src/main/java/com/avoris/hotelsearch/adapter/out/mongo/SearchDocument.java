package com.avoris.hotelsearch.adapter.out.mongo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.avoris.hotelsearch.domain.model.Ages;
import com.avoris.hotelsearch.domain.model.HotelId;
import com.avoris.hotelsearch.domain.model.NormalizedSearchKey;
import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.domain.model.SearchId;
import com.avoris.hotelsearch.domain.model.StayDates;

@Document(collection = "hotel_searches")
public class SearchDocument {

    @Id
    private String id;
    private String hotelId;
    private String checkIn;
    private String checkOut;
    private List<Integer> ages;
    private String normalizedKey;

    public SearchDocument() {
    }

    public SearchDocument(final String id, final String hotelId, final String checkIn, final String checkOut, final List<Integer> ages,
            final String normalizedKey) {
        this.id = id;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages;
        this.normalizedKey = normalizedKey;
    }

    public static SearchDocument from(final Search search) {
        return new SearchDocument(search.id().value(), search.hotelId().value(), search.stayDates().checkIn(), search.stayDates().checkOut(),
                search.ages().values(), search.normalizedKey().value());
    }

    public Search toDomain() {
        return new Search(new SearchId(id), new HotelId(hotelId), new StayDates(checkIn, checkOut), new Ages(ages), new NormalizedSearchKey(normalizedKey));
    }

    public String getId() {
        return id;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public List<Integer> getAges() {
        return ages;
    }

    public String getNormalizedKey() {
        return normalizedKey;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setHotelId(final String hotelId) {
        this.hotelId = hotelId;
    }

    public void setCheckIn(final String checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(final String checkOut) {
        this.checkOut = checkOut;
    }

    public void setAges(final List<Integer> ages) {
        this.ages = ages;
    }

    public void setNormalizedKey(final String normalizedKey) {
        this.normalizedKey = normalizedKey;
    }
}
