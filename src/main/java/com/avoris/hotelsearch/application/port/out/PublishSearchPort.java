package com.avoris.hotelsearch.application.port.out;

import com.avoris.hotelsearch.domain.model.Search;

public interface PublishSearchPort {
    void publish(Search search);
}
