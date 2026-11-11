package br.com.moonspace.cdc.connector.core.usecases.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.ports.inbound.driver.delete.DeleteDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.delete.DeleteDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.DeleteDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DeleteDriverUseCase implements DeleteDriverCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDriverUseCase.class);

    private final DeleteDriverOutboundPort deleteDriverOutboundPort;
    private final GetDriverOutboundPort getDriverOutboundPort;

    public DeleteDriverUseCase(DeleteDriverOutboundPort deleteDriverOutboundPort,
                               GetDriverOutboundPort getDriverOutboundPort) {
        this.deleteDriverOutboundPort = Objects.requireNonNull(deleteDriverOutboundPort);
        this.getDriverOutboundPort = Objects.requireNonNull(getDriverOutboundPort);
    }

    @Override
    public void execute(DeleteDriverInboundCommand command) {
        LOG.atInfo()
                .setMessage("Searching driver Identify '{}'")
                .addArgument(command.id())
                .log();
        var aggregate = getDriverOutboundPort.execute(DriverID.from(command.id()));
        LOG.atInfo()
                .setMessage("Successfully found driver")
                .log();
        this.deleteDriverOutboundPort.execute(aggregate.getId());
    }

}