package br.com.moonspace.commons.rest;

import br.com.moonspace.commons.rest.response.ApiError;
import br.com.moonspace.commons.rest.response.ApiSubError;
import br.com.moonspace.commons.rest.response.ApiValidationError;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;

public class CommunicationExceptionHandler {

    private CommunicationExceptionHandler() {
    }

    public static ApiError handleException(DomainException e) {
        var apiError = new ApiError(e.getMessage());
        var errors = e.getErrors()
                .stream()
                .map(error -> ApiValidationError.of(error.error()))
                .map(ApiSubError.class::cast)
                .toList();
        apiError.addSubError(errors);
        return apiError;
    }

}