package com.avoris.hotelsearch.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avoris.hotelsearch.application.port.in.PersistSearchUseCase;
import com.avoris.hotelsearch.application.port.out.SaveSearchPort;
import com.avoris.hotelsearch.domain.model.Search;

@Service
public class PersistSearchService implements PersistSearchUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistSearchService.class);

    private final SaveSearchPort saveSearchPort;

    public PersistSearchService(final SaveSearchPort saveSearchPort) {
        this.saveSearchPort = saveSearchPort;
    }

    @Override
    public void persist(final Search search) {
        LOGGER.info("Persisting search id={}", search.id().value());
        saveSearchPort.save(search);
    }
}
