package br.com.moonspace.cdc.router.core.domain.entities.connector;

import br.com.moonspace.core.validation.Error;
import br.com.moonspace.core.validation.ValidationHandler;
import br.com.moonspace.core.validation.handler.ThrowsValidationHandler;

import java.util.List;

public class Connector {

    private final String id;
    private String name;
    private List<String> properties;
    private String driverId;
    private String driverName;

    protected Connector(String id, String name, String driverId, String driverName,
            List<String> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
        this.driverId = driverId;
        this.driverName = driverName;
        validate(new ThrowsValidationHandler());
    }

    public static Connector from(String id, String name, String driverId, String driverName,
                                   List<String> properties) {
        return new Connector(id, name, driverId, driverName, properties);
    }

    public static Connector create(String id, String name, String driverId, String driverName,
                                   List<String> properties) {
        return new Connector(id, name, driverId, driverName, properties);
    }

    public Connector update(String name, String driverId, String driverName, List<String> properties) {
        this.name = name;
        this.properties = properties;
        this.driverId = driverId;
        this.driverName = driverName;
        validate(new ThrowsValidationHandler());
        return this;
    }

    public String getId() {
        return id;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public List<String> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public void validate(final ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (name == null || name.isBlank()) {
            handler.append(new Error("'name' should not be empty"));
        }
    }

}