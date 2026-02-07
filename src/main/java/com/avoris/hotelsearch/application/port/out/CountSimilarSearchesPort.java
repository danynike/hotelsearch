package com.avoris.hotelsearch.application.port.out;

import com.avoris.hotelsearch.domain.model.NormalizedSearchKey;

public interface CountSimilarSearchesPort {
    long countByNormalizedKey(NormalizedSearchKey key);
}
