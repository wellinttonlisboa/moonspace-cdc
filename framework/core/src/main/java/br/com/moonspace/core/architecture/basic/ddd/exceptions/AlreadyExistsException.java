package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.validation.Error;

import java.util.Collections;
import java.util.List;

public class AlreadyExistsException extends DomainException {

    protected AlreadyExistsException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static AlreadyExistsException of(String aggregate, String name) {
        final var error = "%s with Name %s already exists".formatted(
                aggregate,
                name
        );
        return new AlreadyExistsException(error, Collections.emptyList());
    }

}