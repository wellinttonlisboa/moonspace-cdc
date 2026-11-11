package br.com.moonspace.cdc.router.ports.inbound.connector.create;

import java.util.List;

public record CreateConnectorInboundCommand(
        String id,
        String name,
        String driverId,
        String driverName,
        List<String> properties
) {

    public static CreateConnectorInboundCommand from(String id, String name, String driverId, String driverName,
                                                     List<String> properties) {
        return new CreateConnectorInboundCommand(id, name, driverId, driverName, properties);
    }

}