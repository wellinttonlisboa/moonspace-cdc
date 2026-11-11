package br.com.moonspace.cdc.connector.adapters.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundQuery;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.PageableConnectorOutboundPort;
import br.com.moonspace.commons.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PageableConnectorOutboundAdapter implements PageableConnectorOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(PageableConnectorOutboundAdapter.class);

    private final ConnectorRepositoryGateway repository;

    public PageableConnectorOutboundAdapter(ConnectorRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Pagination<Connector> execute(PageableConnectorInboundQuery query) {
        LOG.atInfo()
                .setMessage("Searching paginated connector list")
                .log();
        var sort = Sort.by(Sort.Direction.fromString(query.direction()),
            query.sort());
        var page = PageRequest.of(query.page(), query.perPage(), sort);
        var output = this.repository.findAll(page);
        LOG.atInfo()
                .setMessage("Returning successfully")
                .log();
        return output.map(entity -> Connector.from(ConnectorID.from(entity.id()), Driver.from(DriverID.from(entity.driver().id()), entity.name()),
            entity.name(), entity.properties()));
    }

}