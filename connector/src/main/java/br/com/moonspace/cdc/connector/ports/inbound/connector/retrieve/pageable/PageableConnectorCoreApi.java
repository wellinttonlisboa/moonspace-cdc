package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable;

import br.com.moonspace.commons.pagination.Pagination;

public interface PageableConnectorCoreApi {
    Pagination<PageableConnectorInboundOutput> execute(PageableConnectorInboundQuery query);
}