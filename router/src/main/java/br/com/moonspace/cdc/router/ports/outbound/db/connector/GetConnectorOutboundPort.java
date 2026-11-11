package br.com.moonspace.cdc.router.ports.outbound.db.connector;

import br.com.moonspace.cdc.router.core.domain.entities.connector.Connector;

public interface GetConnectorOutboundPort {
    Connector execute(String id);
}