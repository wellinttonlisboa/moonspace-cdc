package br.com.moonspace.cdc.connector.adapters.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepositoryGateway;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.ListDriverOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ListDriverOutboundAdapter implements ListDriverOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(ListDriverOutboundAdapter.class);

    private final DriverRepositoryGateway repository;

    public ListDriverOutboundAdapter(DriverRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public List<Driver> execute() {
        LOG.atInfo()
                .setMessage("Searching driver list")
                .log();
        var output = this.repository.findAll();
        LOG.atInfo()
                .setMessage("Returning successfully")
                .log();
        return output.stream()
                .map(entity -> Driver.from(DriverID.from(entity.id()), entity.name()))
                .toList();
    }

}