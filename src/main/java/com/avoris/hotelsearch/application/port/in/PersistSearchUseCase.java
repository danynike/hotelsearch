package com.avoris.hotelsearch.application.port.in;

import com.avoris.hotelsearch.domain.model.Search;

public interface PersistSearchUseCase {
    void persist(Search search);
}
