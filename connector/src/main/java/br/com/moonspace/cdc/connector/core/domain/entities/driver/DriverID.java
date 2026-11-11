package br.com.moonspace.cdc.connector.core.domain.entities.driver;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

import br.com.moonspace.commons.utils.GenId;
import br.com.moonspace.core.architecture.basic.ddd.Identifier;

public class DriverID extends Identifier {

    private final String value;

    private DriverID(String id) {
        ObjectUtils.requireNonEmpty(id, "'driverId' should not be empty");
        this.value = id;
    }

    public static DriverID unique() {
        return DriverID.from(GenId.newId());
    }

    public static DriverID from(String id) {
        return new DriverID(id);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final DriverID that = (DriverID) object;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}