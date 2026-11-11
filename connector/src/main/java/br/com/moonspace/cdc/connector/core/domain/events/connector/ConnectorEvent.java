package br.com.moonspace.cdc.connector.core.domain.events.connector;

import br.com.moonspace.commons.utils.InstantUtils;
import br.com.moonspace.core.architecture.basic.ddd.events.DomainEvent;
import br.com.moonspace.schema.registry.v1.connector.Operation;

import java.time.Instant;
import java.util.List;

public record ConnectorEvent(
        String id,
        String name,
        String driverId,
        String driverName,
        List<String> properties,
        Operation operation,
        Instant occurredOn
) implements DomainEvent {

    public static ConnectorEvent from(String id,
                                      String name,
                                      String driverId,
                                      String driverName,
                                      List<String> properties,
                                      Operation operation) {
        return new ConnectorEvent(
                id,
                name,
                driverId,
                driverName,
                properties,
                operation,
                InstantUtils.now()
        );
    }

    @Override
    public String toString() {
        return operation.name() + " connector '" + id() + "'";
    }

}