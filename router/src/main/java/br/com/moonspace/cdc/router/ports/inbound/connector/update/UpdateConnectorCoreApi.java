package br.com.moonspace.cdc.router.ports.inbound.connector.update;

public interface UpdateConnectorCoreApi {
    UpdateConnectorInboundOutput execute(UpdateConnectorInboundCommand command);
}