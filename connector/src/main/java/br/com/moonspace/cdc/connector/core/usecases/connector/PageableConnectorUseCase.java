package br.com.moonspace.cdc.connector.core.usecases.connector;

import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundQuery;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.PageableConnectorOutboundPort;
import br.com.moonspace.commons.pagination.Pagination;

import java.util.Objects;

public class PageableConnectorUseCase implements PageableConnectorCoreApi {

    private final PageableConnectorOutboundPort pageableConnectorOutboundPort;

    public PageableConnectorUseCase(PageableConnectorOutboundPort pageableConnectorOutboundPort) {
        this.pageableConnectorOutboundPort = Objects.requireNonNull(pageableConnectorOutboundPort);
    }

    @Override
    public Pagination<PageableConnectorInboundOutput> execute(PageableConnectorInboundQuery query) {
        var outputs = pageableConnectorOutboundPort.execute(query);
        return outputs.map(output -> PageableConnectorInboundOutput.from(output.getId().getValue(),
                output.getName(), PageableDriverInboundOutput.from(output.getDriver().getId().getValue(), output.getDriver().getName()),
                output.getProperties()));
    }

}