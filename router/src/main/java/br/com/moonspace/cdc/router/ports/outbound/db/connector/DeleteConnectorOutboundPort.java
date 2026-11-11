package br.com.moonspace.cdc.router.ports.outbound.db.connector;

public interface DeleteConnectorOutboundPort {
    void execute(String id);
}