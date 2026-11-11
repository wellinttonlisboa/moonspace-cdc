package br.com.moonspace.cdc.connector.adapters.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepositoryGateway;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GetDriverOutboundAdapter implements GetDriverOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(GetDriverOutboundAdapter.class);

    private final DriverRepositoryGateway repository;

    public GetDriverOutboundAdapter(DriverRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Driver execute(DriverID id) {
        LOG.atInfo()
                .setMessage("Searching driver")
                .log();
        var output = this.repository.findById(id);
        LOG.atInfo()
                .setMessage("Successfully found driver")
                .log();
        return Driver.from(DriverID.from(output.id()), output.name());
    }

}