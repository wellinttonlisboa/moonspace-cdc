package br.com.moonspace.cdc.connector.ports.inbound.connector.update;

import java.util.List;

public record UpdateConnectorInboundOutput(
        String id,
        String name,
        List<String> properties
) {

    public static UpdateConnectorInboundOutput from(String id, String name, List<String> properties) {
        return new UpdateConnectorInboundOutput(id, name, properties);
    }

}