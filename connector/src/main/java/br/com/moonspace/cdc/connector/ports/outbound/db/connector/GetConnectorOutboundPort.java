package br.com.moonspace.cdc.connector.ports.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;

public interface GetConnectorOutboundPort {
    Connector execute(ConnectorID id);
}