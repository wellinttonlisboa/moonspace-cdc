package br.com.moonspace.cdc.connector.ports.inbound.connector.create;

import java.util.List;

public record CreateConnectorInboundOutput(
        String id,
        String name,
        List<String> properties
) {

    public static CreateConnectorInboundOutput from(String id, String name, List<String> properties) {
        return new CreateConnectorInboundOutput(id, name, properties);
    }

}