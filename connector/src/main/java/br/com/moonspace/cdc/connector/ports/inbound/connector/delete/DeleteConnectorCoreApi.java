package br.com.moonspace.cdc.connector.ports.inbound.connector.delete;

public interface DeleteConnectorCoreApi {
    void execute(DeleteConnectorInboundCommand command);
}