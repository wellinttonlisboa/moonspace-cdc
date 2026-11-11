package br.com.moonspace.cdc.connector.ports.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;

public interface UpdateConnectorOutboundPort {
    Connector execute(Connector aggregate);
}