package com.avoris.hotelsearch.application.port.out;

import java.util.Optional;

import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.domain.model.SearchId;

public interface LoadSearchPort {
    Optional<Search> loadById(SearchId searchId);
}
