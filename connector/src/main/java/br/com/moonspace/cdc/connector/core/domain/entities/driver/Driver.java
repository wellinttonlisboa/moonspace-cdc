package br.com.moonspace.cdc.connector.core.domain.entities.driver;

import br.com.moonspace.core.architecture.basic.ddd.AggregateRoot;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.core.validation.ValidationHandler;
import br.com.moonspace.core.validation.handler.Notification;

public class Driver extends AggregateRoot<DriverID> {

    private String name;

    protected Driver(DriverID id, String name) {
        super(id);
        this.name = name;
        validate();
    }

    public static Driver create(String name) {
        return new Driver(DriverID.unique(), name);
    }

    public Driver update(String name) {
        this.name = name;
        validate();
        return this;
    }

    public static Driver from(DriverID id, String name) {
        return new Driver(id, name);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new DriverNameValidator(this, handler).validate();
    }

    private void validate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate Driver", notification);
        }
    }

}