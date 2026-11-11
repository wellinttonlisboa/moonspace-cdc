package br.com.moonspace.cdc.connector.ports.inbound.connector.create;

public interface CreateConnectorCoreApi {
    CreateConnectorInboundOutput execute(CreateConnectorInboundCommand command);
}