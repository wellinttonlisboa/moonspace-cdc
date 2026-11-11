package br.com.moonspace.cdc.router.ports.inbound.connector.create;

import java.util.List;

public record CreateConnectorInboundOutput(
        String id,
        String name,
        String driverId,
        String driverName,
        List<String> properties
) {

    public static CreateConnectorInboundOutput from(String id, String name, String driverId, String driverName,
                                                    List<String> properties) {
        return new CreateConnectorInboundOutput(id, name, driverId, driverName, properties);
    }

}