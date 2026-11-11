package br.com.moonspace.cdc.router.ports.inbound.connector.update;

import java.util.List;

public record UpdateConnectorInboundOutput(
        String id,
        String name,
        String driverId,
        String driverName,
        List<String> properties
) {

    public static UpdateConnectorInboundOutput from(String id, String name, String driverId, String driverName,
                                                    List<String> properties) {
        return new UpdateConnectorInboundOutput(id, name, driverId, driverName, properties);
    }

}