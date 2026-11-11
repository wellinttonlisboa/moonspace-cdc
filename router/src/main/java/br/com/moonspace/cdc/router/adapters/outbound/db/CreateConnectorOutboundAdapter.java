package br.com.moonspace.cdc.router.adapters.outbound.db;

import br.com.moonspace.cdc.router.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.CreateConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CreateConnectorOutboundAdapter implements CreateConnectorOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(CreateConnectorOutboundAdapter.class);

    private final ConnectorRepositoryGateway repository;

    public CreateConnectorOutboundAdapter(ConnectorRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Connector execute(Connector aggregate) {
        LOG.atInfo()
                .setMessage("Creating connector Identify '{}'")
                .addArgument(aggregate.getId())
                .log();
        var entity = ConnectorEntity.from(aggregate.getId(), aggregate.getName(), aggregate.getDriverId(),
                aggregate.getDriverName(), aggregate.getProperties());
        var output = this.repository.save(entity);
        LOG.atInfo()
                .setMessage("Created successfully")
                .log();
        return Connector.from(output.id(), output.name(), output.driverId(), output.driverName(), output.properties());
    }

}