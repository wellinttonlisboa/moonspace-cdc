package br.com.moonspace.cdc.connector.ports.inbound.connector.update;

public interface UpdateConnectorCoreApi {
    UpdateConnectorInboundOutput execute(UpdateConnectorInboundCommand command);
}