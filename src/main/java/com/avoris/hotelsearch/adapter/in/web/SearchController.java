package com.avoris.hotelsearch.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avoris.hotelsearch.adapter.in.web.dto.SearchCreatedResponse;
import com.avoris.hotelsearch.adapter.in.web.dto.SearchRequestDTO;
import com.avoris.hotelsearch.adapter.in.web.dto.SearchWithCountResponse;
import com.avoris.hotelsearch.application.dto.CreateSearchCommand;
import com.avoris.hotelsearch.application.port.in.CreateSearchUseCase;
import com.avoris.hotelsearch.application.port.in.GetSearchCountUseCase;
import com.avoris.hotelsearch.domain.model.SearchId;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/1")
public class SearchController {

    private final CreateSearchUseCase createSearchUseCase;
    private final GetSearchCountUseCase getSearchCountUseCase;

    public SearchController(final CreateSearchUseCase createSearchUseCase, final GetSearchCountUseCase getSearchCountUseCase) {
        this.createSearchUseCase = createSearchUseCase;
        this.getSearchCountUseCase = getSearchCountUseCase;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchCreatedResponse> createSearch(@Valid @RequestBody final SearchRequestDTO request) {
        final var created = createSearchUseCase.create(new CreateSearchCommand(request.hotelId(), request.checkIn(), request.checkOut(), request.ages()));
        return ResponseEntity.ok(new SearchCreatedResponse(created.searchId()));
    }

    @GetMapping("/count")
    public ResponseEntity<SearchWithCountResponse> count(@RequestParam("searchId") final String searchId) {
        final var result = getSearchCountUseCase.getById(new SearchId(searchId));
        return ResponseEntity.ok(new SearchWithCountResponse(result.searchId(),
                new SearchWithCountResponse.SearchDto(result.hotelId(), result.checkIn(), result.checkOut(), result.agesSorted()), result.count()));
    }
}
