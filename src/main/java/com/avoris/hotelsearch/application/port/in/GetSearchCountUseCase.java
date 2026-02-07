package com.avoris.hotelsearch.application.port.in;

import com.avoris.hotelsearch.application.dto.SearchWithCount;
import com.avoris.hotelsearch.domain.model.SearchId;

public interface GetSearchCountUseCase {
    SearchWithCount getById(SearchId searchId);
}
