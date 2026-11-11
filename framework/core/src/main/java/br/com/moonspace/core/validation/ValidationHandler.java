package br.com.moonspace.core.validation;

import java.util.List;
import java.util.Objects;

public interface ValidationHandler {

    ValidationHandler append(final Error error);

    ValidationHandler append(final ValidationHandler handler);

    <T> T validate(final Validation<T> validation);

    List<Error> getErrors();

    String toString();

    default boolean hasError() {
        return Objects.nonNull(getErrors()) && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (Objects.nonNull(getErrors()) && !getErrors().isEmpty()) {
            return getErrors().getFirst();
        } else {
            return null;
        }
    }

    interface Validation<T> {
        T validate();
    }

}