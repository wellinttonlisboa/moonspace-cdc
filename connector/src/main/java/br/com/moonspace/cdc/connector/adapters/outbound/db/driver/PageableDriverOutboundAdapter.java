package br.com.moonspace.cdc.connector.adapters.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepositoryGateway;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundQuery;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.PageableDriverOutboundPort;
import br.com.moonspace.commons.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PageableDriverOutboundAdapter implements PageableDriverOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(PageableDriverOutboundAdapter.class);

    private final DriverRepositoryGateway repository;

    public PageableDriverOutboundAdapter(DriverRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Pagination<Driver> execute(PageableDriverInboundQuery query) {
        LOG.atInfo()
                .setMessage("Searching paginated driver list")
                .log();
        var sort = Sort.by(Sort.Direction.fromString(query.direction()),
            query.sort());
        var page = PageRequest.of(query.page(), query.perPage(), sort);
        var output = this.repository.findAll(page);
        LOG.atInfo()
                .setMessage("Returning successfully")
                .log();
        return output.map(entity -> Driver.from(DriverID.from(entity.id()), entity.name()));
    }

}