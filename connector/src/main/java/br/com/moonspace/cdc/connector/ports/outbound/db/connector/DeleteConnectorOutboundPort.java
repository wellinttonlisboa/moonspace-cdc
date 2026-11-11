package br.com.moonspace.cdc.connector.ports.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;

public interface DeleteConnectorOutboundPort {
    void execute(ConnectorID id);
}