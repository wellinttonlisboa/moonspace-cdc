package br.com.moonspace.cdc.connector.ports.inbound.connector.update;

import java.util.List;

public record UpdateConnectorInboundCommand(
        String id,
        String name,
        List<String> properties
) {

}