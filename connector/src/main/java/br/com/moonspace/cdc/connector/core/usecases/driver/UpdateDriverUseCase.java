package br.com.moonspace.cdc.connector.core.usecases.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.UpdateDriverOutboundPort;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.core.validation.handler.Notification;

import java.util.Objects;

public class UpdateDriverUseCase implements UpdateDriverCoreApi {

    private final UpdateDriverOutboundPort updateDriverOutboundPort;
    private final GetDriverOutboundPort getDriverOutboundPort;

    public UpdateDriverUseCase(UpdateDriverOutboundPort updateDriverOutboundPort,
                               GetDriverOutboundPort getDriverOutboundPort) {
        this.updateDriverOutboundPort = Objects.requireNonNull(updateDriverOutboundPort);
        this.getDriverOutboundPort = Objects.requireNonNull(getDriverOutboundPort);
    }

    @Override
    public UpdateDriverInboundOutput execute(UpdateDriverInboundCommand command) {
        var aggregate = getDriverOutboundPort.execute(DriverID.from(command.id()));
        var notification = Notification.create();
        notification.validate(() -> aggregate.update(command.name()));
        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate driver %s".formatted(command.id()), notification);
        }
        var output = this.updateDriverOutboundPort.execute(aggregate);
        return UpdateDriverInboundOutput.from(output.getId().getValue(), output.getName());
    }

}