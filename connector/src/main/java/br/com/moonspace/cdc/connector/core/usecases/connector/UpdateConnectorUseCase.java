package br.com.moonspace.cdc.connector.core.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.queue.connector.ProduceConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.core.domain.events.connector.ConnectorEvent;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.GetConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.UpdateConnectorOutboundPort;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.core.validation.handler.Notification;
import br.com.moonspace.schema.registry.v1.connector.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class UpdateConnectorUseCase implements UpdateConnectorCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateConnectorUseCase.class);

    private final UpdateConnectorOutboundPort updateConnectorOutboundPort;
    private final GetConnectorOutboundPort getConnectorOutboundPort;
    private final ProduceConnectorOutboundAdapter produceConnectorOutboundAdapter;

    public UpdateConnectorUseCase(UpdateConnectorOutboundPort updateConnectorOutboundPort,
                                  GetConnectorOutboundPort getConnectorOutboundPort,
                                  ProduceConnectorOutboundAdapter produceConnectorOutboundAdapter) {
        this.updateConnectorOutboundPort = Objects.requireNonNull(updateConnectorOutboundPort);
        this.getConnectorOutboundPort = Objects.requireNonNull(getConnectorOutboundPort);
        this.produceConnectorOutboundAdapter = Objects.requireNonNull(produceConnectorOutboundAdapter);
    }

    @Override
    public UpdateConnectorInboundOutput execute(UpdateConnectorInboundCommand command) {
        var aggregate = getConnectorOutboundPort.execute(ConnectorID.from(command.id()));
        LOG.atInfo()
                .setMessage("Validating connector")
                .log();
        var notification = Notification.create();
        notification.validate(() -> aggregate.update(command.name(), command.properties()));
        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate connector %s".formatted(command.id()), notification);
        }
        LOG.atInfo()
                .setMessage("Validated")
                .log();
        var output = this.updateConnectorOutboundPort.execute(aggregate);
        var event = ConnectorEvent.from(output.getId().getValue(), output.getName(), output.getDriver().getId().getValue(),
                output.getDriver().getName(), output.getProperties(), Operation.UPDATED);
        this.produceConnectorOutboundAdapter.execute(event);
        return UpdateConnectorInboundOutput.from(output.getId().getValue(), output.getName(),
                output.getProperties());
    }

}