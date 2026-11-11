package br.com.moonspace.cdc.router.ports.inbound.connector.update;

import java.util.List;

public record UpdateConnectorInboundCommand(
        String id,
        String name,
        String driverId,
        String driverName,
        List<String> properties
) {

    public static UpdateConnectorInboundCommand from(String id, String name, String driverId, String driverName,
                                                     List<String> properties) {
        return new UpdateConnectorInboundCommand(id, name, driverId, driverName, properties);
    }

}