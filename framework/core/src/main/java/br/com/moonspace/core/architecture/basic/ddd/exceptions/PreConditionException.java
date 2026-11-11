package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.validation.Error;

import java.util.Collections;
import java.util.List;

public class PreConditionException extends DomainException {

    protected PreConditionException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static PreConditionException of(String message) {
        return new PreConditionException(message, Collections.emptyList());
    }

}