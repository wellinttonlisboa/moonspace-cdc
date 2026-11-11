package br.com.moonspace.cdc.connector.core.domain.entities.connector;

import br.com.moonspace.commons.utils.GenId;
import br.com.moonspace.core.architecture.basic.ddd.Identifier;

import java.util.Objects;

public class ConnectorID extends Identifier {

    private final String value;

    private ConnectorID(String id) {
        Objects.requireNonNull(id);
        this.value = id;
    }

    public static ConnectorID unique() {
        return ConnectorID.from(GenId.newId());
    }

    public static ConnectorID from(String id) {
        return new ConnectorID(id);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final ConnectorID that = (ConnectorID) object;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}