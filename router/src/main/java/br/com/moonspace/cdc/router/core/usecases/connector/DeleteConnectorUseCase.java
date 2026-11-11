package br.com.moonspace.cdc.router.core.usecases.connector;

import br.com.moonspace.cdc.router.ports.inbound.connector.delete.DeleteConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.delete.DeleteConnectorInboundCommand;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.DeleteConnectorOutboundPort;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.GetConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DeleteConnectorUseCase implements DeleteConnectorCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteConnectorUseCase.class);

    private final DeleteConnectorOutboundPort deleteConnectorOutboundPort;
    private final GetConnectorOutboundPort getConnectorOutboundPort;

    public DeleteConnectorUseCase(DeleteConnectorOutboundPort deleteConnectorOutboundPort,
                                  GetConnectorOutboundPort getConnectorOutboundPort) {
        this.deleteConnectorOutboundPort = Objects.requireNonNull(deleteConnectorOutboundPort);
        this.getConnectorOutboundPort = Objects.requireNonNull(getConnectorOutboundPort);
    }

    @Override
    public void execute(DeleteConnectorInboundCommand command) {
        LOG.atInfo()
                .setMessage("Searching connector Identify '{}'")
                .addArgument(command.id())
                .log();
        var aggregate = getConnectorOutboundPort.execute(command.id());
        LOG.atInfo()
                .setMessage("Successfully found connector")
                .log();
        this.deleteConnectorOutboundPort.execute(aggregate.getId());
    }

}