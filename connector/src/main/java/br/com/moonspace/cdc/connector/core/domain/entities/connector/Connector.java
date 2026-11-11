package br.com.moonspace.cdc.connector.core.domain.entities.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.core.architecture.basic.ddd.AggregateRoot;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.core.validation.ValidationHandler;
import br.com.moonspace.core.validation.handler.Notification;

import java.util.List;

public class Connector extends AggregateRoot<ConnectorID> {

    private String name;
    private List<String> properties;
    private final Driver driver;

    protected Connector(ConnectorID id, Driver driver, String name,
                        List<String> properties) {
        super(id);
        this.driver = driver;
        this.name = name;
        this.properties = properties;
        validate();
    }

    public static Connector create(Driver driver, String name,
                                   List<String> properties) {
        return new Connector(ConnectorID.unique(), driver, name, properties);
    }

    public Connector update(String name, List<String> properties) {
        this.name = name;
        this.properties = properties;
        validate();
        return this;
    }

    public static Connector from(ConnectorID id, Driver driver, String name,
                                 List<String> properties) {
        return new Connector(id, driver, name, properties);
    }

    public Driver getDriver() {
        return this.driver;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getProperties() {
        return this.properties;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new ConnectorNameValidator(this, handler).validate();
        new ConnectorPropertiesValidator(this, handler).validate();
    }

    private void validate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate Connector", notification);
        }
    }

}