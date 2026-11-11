package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.validation.Error;

import java.util.Collections;
import java.util.List;

public class InternalException extends DomainException {

    protected InternalException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static InternalException of(String message) {
        return new InternalException(message, Collections.emptyList());
    }

}
