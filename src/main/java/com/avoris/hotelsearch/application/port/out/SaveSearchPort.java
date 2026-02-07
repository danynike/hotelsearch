package com.avoris.hotelsearch.application.port.out;

import com.avoris.hotelsearch.domain.model.Search;

public interface SaveSearchPort {
    void save(Search search);
}
