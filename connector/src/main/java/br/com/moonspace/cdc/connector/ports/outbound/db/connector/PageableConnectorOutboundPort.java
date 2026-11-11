package br.com.moonspace.cdc.connector.ports.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundQuery;
import br.com.moonspace.commons.pagination.Pagination;

public interface PageableConnectorOutboundPort {
    Pagination<Connector> execute(PageableConnectorInboundQuery query);
}