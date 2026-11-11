package br.com.moonspace.cdc.connector.adapters.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.UpdateConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UpdateConnectorOutboundAdapter implements UpdateConnectorOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateConnectorOutboundAdapter.class);

    private final ConnectorRepositoryGateway repository;

    public UpdateConnectorOutboundAdapter(ConnectorRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Connector execute(Connector aggregate) {
        LOG.atInfo()
                .setMessage("Updating connector Identify '{}'")
                .addArgument(aggregate.getId().getValue())
                .log();
        var driverEntity = DriverEntity.from(aggregate.getDriver().getId().getValue(),
                aggregate.getDriver().getName());
        var entity = ConnectorEntity.from(aggregate.getId().getValue(), aggregate.getName(),
                aggregate.getProperties(), driverEntity);
        var output = this.repository.save(entity);
        LOG.atInfo()
                .setMessage("Updated successfully")
                .log();
        return Connector.from(ConnectorID.from(output.id()), Driver.from(DriverID.from(output.driver().id()), output.driver().name()),
                output.name(), output.properties());
    }

}