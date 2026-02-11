package com.avoris.hotelsearch.infrastructure.adapter.out.mongo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    private LocalDate checkIn;

    private LocalDate checkOut;

    private List<Integer> ages;

    @Indexed(name = "idx_normalized_key")
    private String normalizedKey;

    public SearchDocument() {
    }

    public SearchDocument(final String id, final String hotelId, final LocalDate checkIn, final LocalDate checkOut, final List<Integer> ages,
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

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
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

    public void setCheckIn(final LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(final LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public void setAges(final List<Integer> ages) {
        this.ages = ages;
    }

    public void setNormalizedKey(final String normalizedKey) {
        this.normalizedKey = normalizedKey;
    }

}
