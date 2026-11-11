package br.com.moonspace.cdc.router.ports.inbound.connector.create;

public interface CreateConnectorCoreApi {
    CreateConnectorInboundOutput execute(CreateConnectorInboundCommand command);
}