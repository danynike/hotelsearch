package com.avoris.hotelsearch.domain.exception;

public class SearchNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5006712946031704344L;

    public SearchNotFoundException(final String message) {
        super(message);
    }
}
