package br.com.moonspace.cdc.connector.ports.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundQuery;
import br.com.moonspace.commons.pagination.Pagination;

public interface PageableDriverOutboundPort {
    Pagination<Driver> execute(PageableDriverInboundQuery query);
}