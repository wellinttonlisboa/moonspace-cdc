package br.com.moonspace.cdc.connector.core.domain.entities.connector;

import br.com.moonspace.core.validation.Error;
import br.com.moonspace.core.validation.ValidationHandler;
import br.com.moonspace.core.validation.Validator;

import java.util.Objects;

public class ConnectorNameValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;

    private final Connector aggregate;

    public ConnectorNameValidator(Connector aggregate, ValidationHandler handler) {
        super(handler);
        this.aggregate = aggregate;
    }

    @Override
    public void validate() {
        var name = aggregate.getName();
        if (Objects.isNull(name)) {
            validationHandler().append(Error.of("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            validationHandler().append(Error.of("'name' should not be empty"));
            return;
        }

        int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            validationHandler().append(Error.of("'name' must be between 3 and 255 characters"));
        }
    }

}