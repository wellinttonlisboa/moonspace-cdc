package br.com.moonspace.core.validation.handler;

import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.validation.Error;
import br.com.moonspace.core.validation.ValidationHandler;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(Lists.newArrayList());
    }

    public static Notification create(final Throwable cause) {
        return create(new Error(cause.getMessage()));
    }

    public static Notification create(final Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public Notification append(final Error error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(final Validation<T> validation) {
        try {
            return validation.validate();
        } catch (DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (Exception e) {
            this.errors.add(new Error(e.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

    @Override
    public String toString() {
        StringBuilder strings = new StringBuilder();
        errors.forEach(error -> strings.append(error.error()).append(", "));
        return strings.toString();
    }

}