package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable;

import br.com.moonspace.commons.pagination.Pagination;

public interface PageableDriverCoreApi {
    Pagination<PageableDriverInboundOutput> execute(PageableDriverInboundQuery query);
}