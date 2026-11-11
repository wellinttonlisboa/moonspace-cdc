package br.com.moonspace.cdc.connector.adapters.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.ListConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ListConnectorOutboundAdapter implements ListConnectorOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(ListConnectorOutboundAdapter.class);

    private final ConnectorRepositoryGateway repository;

    public ListConnectorOutboundAdapter(ConnectorRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public List<Connector> execute() {
        LOG.atInfo()
                .setMessage("Searching connector list")
                .log();
        var output = this.repository.findAll();
        LOG.atInfo()
                .setMessage("Returning successfully")
                .log();
        return output.stream()
                .map(entity -> Connector.from(ConnectorID.from(entity.id()), Driver.from(DriverID.from(entity.driver().id()), entity.driver().name()),
                        entity.name(), entity.properties()))
                .toList();
    }

}