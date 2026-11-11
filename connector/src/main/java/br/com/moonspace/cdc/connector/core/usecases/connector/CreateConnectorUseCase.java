package br.com.moonspace.cdc.connector.core.usecases.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.core.domain.events.connector.ConnectorEvent;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.CreateConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.queue.ProduceConnectorOutboundPort;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.core.validation.handler.Notification;
import br.com.moonspace.schema.registry.v1.connector.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CreateConnectorUseCase implements CreateConnectorCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(CreateConnectorUseCase.class);

    private final CreateConnectorOutboundPort createConnectorOutboundPort;
    private final GetDriverOutboundPort getDriverOutboundPort;
    private final ProduceConnectorOutboundPort produceConnectorOutboundPort;

    public CreateConnectorUseCase(CreateConnectorOutboundPort createConnectorOutboundPort,
                                  GetDriverOutboundPort getDriverOutboundPort,
                                  ProduceConnectorOutboundPort produceConnectorOutboundPort) {
        this.createConnectorOutboundPort = Objects.requireNonNull(createConnectorOutboundPort);
        this.getDriverOutboundPort = Objects.requireNonNull(getDriverOutboundPort);
        this.produceConnectorOutboundPort = Objects.requireNonNull(produceConnectorOutboundPort);
    }

    @Override
    public CreateConnectorInboundOutput execute(CreateConnectorInboundCommand command) {
        LOG.atInfo()
                .setMessage("Validating connector")
                .log();
        var notification = Notification.create();
        var driver = this.getDriverOutboundPort.execute(DriverID.from(command.driverId()));
        var aggregate = notification.validate(() -> Connector.create(driver,
                command.name(), command.properties()));
        if (notification.hasError()) {
            LOG.atError()
                    .setMessage("Could not create Aggregate connector: '{}'")
                    .addArgument(notification.toString())
                    .log();
            throw new NotificationException("Could not create Aggregate connector", notification);
        }
        LOG.atInfo()
                .setMessage("Validated")
                .log();
        var output = this.createConnectorOutboundPort.execute(aggregate);
        var event = ConnectorEvent.from(output.getId().getValue(), output.getName(), output.getDriver().getId().getValue(),
                output.getDriver().getName(), output.getProperties(), Operation.CREATED);
        this.produceConnectorOutboundPort.execute(event);
        return CreateConnectorInboundOutput.from(output.getId().getValue(), output.getName(),
                output.getProperties());
    }

}