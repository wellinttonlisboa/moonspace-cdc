package br.com.moonspace.cdc.connector.adapters.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepositoryGateway;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.DeleteDriverOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DeleteDriverOutboundAdapter implements DeleteDriverOutboundPort {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDriverOutboundAdapter.class);

    private final DriverRepositoryGateway repository;

    public DeleteDriverOutboundAdapter(DriverRepositoryGateway repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void execute(DriverID id) {
        LOG.atInfo()
                .setMessage("Deleting driver Identify '{}'")
                .addArgument(id.getValue())
                .log();
        this.repository.deleteById(id);
        LOG.atInfo()
                .setMessage("Deleted successfully")
                .log();
    }

}