package br.com.moonspace.cdc.connector.core.domain.entities.connector;

import br.com.moonspace.core.validation.Error;
import br.com.moonspace.core.validation.ValidationHandler;
import br.com.moonspace.core.validation.Validator;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

public class ConnectorPropertiesValidator extends Validator {

    private final Connector aggregate;

    public ConnectorPropertiesValidator(Connector aggregate, ValidationHandler handler) {
        super(handler);
        this.aggregate = aggregate;
    }

    @Override
    public void validate() {
        var value = this.aggregate.getProperties();
        if (Objects.isNull(value)) {
            validationHandler().append(Error.of("'properties' should not be null"));
            return;
        }

        if (CollectionUtils.isEmpty(value)) {
            validationHandler().append(Error.of("'properties' should not be empty"));
        }

    }

}