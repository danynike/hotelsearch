package com.avoris.hotelsearch.infrastructure.adapter.out.mongo;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.avoris.hotelsearch.application.port.out.CountSimilarSearchesPort;
import com.avoris.hotelsearch.application.port.out.LoadSearchPort;
import com.avoris.hotelsearch.application.port.out.SaveSearchPort;
import com.avoris.hotelsearch.domain.model.NormalizedSearchKey;
import com.avoris.hotelsearch.domain.model.Search;
import com.avoris.hotelsearch.domain.model.SearchId;

@Component
public class SearchMongoRepositoryAdapter implements SaveSearchPort, LoadSearchPort, CountSimilarSearchesPort {

    private final SpringDataSearchRepository repository;

    public SearchMongoRepositoryAdapter(final SpringDataSearchRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(final Search search) {
        repository.save(SearchDocument.from(search));
    }

    @Override
    public Optional<Search> loadById(final SearchId searchId) {
        return repository.findById(searchId.value()).map(SearchDocument::toDomain);
    }

    @Override
    public long countByNormalizedKey(final NormalizedSearchKey key) {
        return repository.countByNormalizedKey(key.value());
    }
}
