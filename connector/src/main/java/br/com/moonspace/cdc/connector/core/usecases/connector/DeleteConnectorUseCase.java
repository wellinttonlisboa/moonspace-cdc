package br.com.moonspace.cdc.connector.core.usecases.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.core.domain.events.connector.ConnectorEvent;
import br.com.moonspace.cdc.connector.ports.inbound.connector.delete.DeleteConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.delete.DeleteConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.DeleteConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.GetConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.queue.ProduceConnectorOutboundPort;
import br.com.moonspace.schema.registry.v1.connector.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DeleteConnectorUseCase implements DeleteConnectorCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteConnectorUseCase.class);

    private final DeleteConnectorOutboundPort deleteConnectorOutboundPort;
    private final GetConnectorOutboundPort getConnectorOutboundPort;
    private final ProduceConnectorOutboundPort produceConnectorOutboundPort;

    public DeleteConnectorUseCase(DeleteConnectorOutboundPort deleteConnectorOutboundPort,
                                  GetConnectorOutboundPort getConnectorOutboundPort,
                                  ProduceConnectorOutboundPort produceConnectorOutboundPort) {
        this.deleteConnectorOutboundPort = Objects.requireNonNull(deleteConnectorOutboundPort);
        this.getConnectorOutboundPort = Objects.requireNonNull(getConnectorOutboundPort);
        this.produceConnectorOutboundPort = Objects.requireNonNull(produceConnectorOutboundPort);
    }

    @Override
    public void execute(DeleteConnectorInboundCommand command) {
        LOG.atInfo()
                .setMessage("Searching connector Identify '{}'")
                .addArgument(command.id())
                .log();
        var output = getConnectorOutboundPort.execute(ConnectorID.from(command.id()));
        LOG.atInfo()
                .setMessage("Successfully found connector")
                .log();
        this.deleteConnectorOutboundPort.execute(output.getId());
        var event = ConnectorEvent.from(output.getId().getValue(), output.getName(), output.getDriver().getId().getValue(),
                output.getDriver().getName(), output.getProperties(), Operation.DELETED);
        this.produceConnectorOutboundPort.execute(event);
    }

}