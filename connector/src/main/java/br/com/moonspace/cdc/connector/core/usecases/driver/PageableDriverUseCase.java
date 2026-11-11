package br.com.moonspace.cdc.connector.core.usecases.driver;

import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundQuery;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.PageableDriverOutboundPort;
import br.com.moonspace.commons.pagination.Pagination;

import java.util.Objects;

public class PageableDriverUseCase implements PageableDriverCoreApi {

    private final PageableDriverOutboundPort pageableDriverOutboundPort;

    public PageableDriverUseCase(PageableDriverOutboundPort pageableDriverOutboundPort) {
        this.pageableDriverOutboundPort = Objects.requireNonNull(pageableDriverOutboundPort);
    }

    @Override
    public Pagination<PageableDriverInboundOutput> execute(PageableDriverInboundQuery query) {
        var aggregates = pageableDriverOutboundPort.execute(query);
        return aggregates.map(aggregate -> PageableDriverInboundOutput.from(aggregate.getId().getValue(),
                aggregate.getName()));
    }

}