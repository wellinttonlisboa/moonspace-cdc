package br.com.moonspace.cdc.connector.adapters.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepositoryGateway;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.CreateDriverOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CreateDriverOutboundAdapter implements CreateDriverOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDriverOutboundAdapter.class);

    private final DriverRepositoryGateway repository;

    public CreateDriverOutboundAdapter(DriverRepositoryGateway driverRepository) {
        this.repository = Objects.requireNonNull(driverRepository);
    }

    @Override
    public Driver execute(Driver aggregate) {
        LOG.atInfo()
                .setMessage("Creating driver Identify '{}'")
                .addArgument(aggregate.getId().getValue())
                .log();
        var entity = DriverEntity.from(aggregate.getId().getValue(), aggregate.getName());
        var output = this.repository.save(entity);
        LOG.atInfo()
                .setMessage("Created successfully")
                .log();
        return Driver.from(DriverID.from(output.id()), output.name());
    }

}