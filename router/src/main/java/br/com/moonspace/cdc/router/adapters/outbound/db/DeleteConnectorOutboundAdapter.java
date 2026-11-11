package br.com.moonspace.cdc.router.adapters.outbound.db;

import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.DeleteConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DeleteConnectorOutboundAdapter implements DeleteConnectorOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteConnectorOutboundAdapter.class);

    private final ConnectorRepositoryGateway repository;

    public DeleteConnectorOutboundAdapter(ConnectorRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void execute(String id) {
        LOG.atInfo()
                .setMessage("Deleting connector Identify '{}'")
                .addArgument(id)
                .log();
        this.repository.deleteById(id);
        LOG.atInfo()
                .setMessage("Deleted successfully")
                .log();
    }

}