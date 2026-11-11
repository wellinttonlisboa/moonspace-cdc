package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.validation.Error;

import java.util.Collections;
import java.util.List;

public class BadRequestException extends DomainException {

    protected BadRequestException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static BadRequestException of(String message) {
        return new BadRequestException(message, Collections.emptyList());
    }

}