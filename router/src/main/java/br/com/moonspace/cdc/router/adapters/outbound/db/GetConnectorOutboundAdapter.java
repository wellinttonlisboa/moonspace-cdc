package br.com.moonspace.cdc.router.adapters.outbound.db;

import br.com.moonspace.cdc.router.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.GetConnectorOutboundPort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GetConnectorOutboundAdapter implements GetConnectorOutboundPort {

    private final ConnectorRepositoryGateway repository;

    public GetConnectorOutboundAdapter(ConnectorRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Connector execute(String id) {
        var output = this.repository.findById(id);
        return Connector.from(output.id(), output.name(), output.driverId(), output.driverName(), output.properties());
    }

}