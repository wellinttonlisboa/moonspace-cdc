package br.com.moonspace.cdc.connector.ports.inbound.connector.create;

import java.util.List;

public record CreateConnectorInboundCommand(
        String driverId,
        String name,
        List<String> properties
) {

}