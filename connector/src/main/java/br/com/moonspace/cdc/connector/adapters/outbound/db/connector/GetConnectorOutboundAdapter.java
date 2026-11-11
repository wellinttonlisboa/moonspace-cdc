package br.com.moonspace.cdc.connector.adapters.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.GetConnectorOutboundPort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GetConnectorOutboundAdapter implements GetConnectorOutboundPort {

    private final ConnectorRepositoryGateway repository;

    public GetConnectorOutboundAdapter(ConnectorRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Connector execute(ConnectorID id) {
        var output = this.repository.findById(id);
        return Connector.from(ConnectorID.from(output.id()), Driver.from(DriverID.from(output.driver().id()),
                        output.driver().name()), output.name(), output.properties());
    }

}