package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.validation.Error;

import java.util.Collections;
import java.util.List;

public class UnauthorizedException extends DomainException {

    protected UnauthorizedException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static UnauthorizedException of(String message) {
        return new UnauthorizedException(message, Collections.emptyList());
    }

}