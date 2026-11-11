package br.com.moonspace.cdc.router.ports.inbound.connector.delete;

public interface DeleteConnectorCoreApi {
    void execute(DeleteConnectorInboundCommand command);
}