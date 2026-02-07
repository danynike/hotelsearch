package com.avoris.hotelsearch.adapter.out.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataSearchRepository extends MongoRepository<SearchDocument, String> {
    long countByNormalizedKey(String normalizedKey);
}
