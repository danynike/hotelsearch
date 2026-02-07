package com.avoris.hotelsearch.application.port.in;

import com.avoris.hotelsearch.application.dto.CreateSearchCommand;
import com.avoris.hotelsearch.application.dto.CreatedSearch;

public interface CreateSearchUseCase {
    CreatedSearch create(CreateSearchCommand command);
}
