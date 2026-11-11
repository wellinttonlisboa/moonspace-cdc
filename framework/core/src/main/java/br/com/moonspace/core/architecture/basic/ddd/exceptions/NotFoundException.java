package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.architecture.basic.ddd.AggregateRoot;
import br.com.moonspace.core.architecture.basic.ddd.Identifier;
import br.com.moonspace.core.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException of(Class<? extends AggregateRoot<?>> aggregate, Identifier id) {
        final var error = "%s with ID %s was not found".formatted(
                aggregate.getSimpleName(),
                id.getValue()
        );
        return new NotFoundException(error, Collections.emptyList());
    }

}