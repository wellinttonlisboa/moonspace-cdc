package br.com.moonspace.cdc.connector.core.usecases.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.CreateDriverOutboundPort;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.core.validation.handler.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CreateDriverUseCase implements CreateDriverCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDriverUseCase.class);

    private final CreateDriverOutboundPort createDriverOutboundPort;

    public CreateDriverUseCase(final CreateDriverOutboundPort createDriverOutboundPort) {
        this.createDriverOutboundPort = Objects.requireNonNull(createDriverOutboundPort);
    }

    @Override
    public CreateDriverInboundOutput execute(CreateDriverInboundCommand command) {
        LOG.atInfo()
                .setMessage("Validating driver")
                .log();
        var notification = Notification.create();
        var aggregate = notification.validate(() -> Driver.create(command.name()));
        if (notification.hasError()) {
            LOG.atError()
                    .setMessage("Could not create Aggregate driver: '{}'")
                    .addArgument(notification.toString())
                    .log();
            throw new NotificationException("Could not create Aggregate driver", notification);
        }
        LOG.atInfo()
                .setMessage("Validated")
                .log();
        var output = this.createDriverOutboundPort.execute(aggregate);
        return CreateDriverInboundOutput.from(output.getId().getValue(),
                output.getName());
    }

}