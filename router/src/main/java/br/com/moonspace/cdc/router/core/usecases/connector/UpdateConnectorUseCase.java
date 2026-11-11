package br.com.moonspace.cdc.router.core.usecases.connector;

import br.com.moonspace.cdc.router.ports.inbound.connector.update.UpdateConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.update.UpdateConnectorInboundCommand;
import br.com.moonspace.cdc.router.ports.inbound.connector.update.UpdateConnectorInboundOutput;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.GetConnectorOutboundPort;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.UpdateConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class UpdateConnectorUseCase implements UpdateConnectorCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateConnectorUseCase.class);

    private final UpdateConnectorOutboundPort updateConnectorOutboundPort;
    private final GetConnectorOutboundPort getConnectorOutboundPort;

    public UpdateConnectorUseCase(UpdateConnectorOutboundPort updateConnectorOutboundPort,
                                  GetConnectorOutboundPort getConnectorOutboundPort) {
        this.updateConnectorOutboundPort = Objects.requireNonNull(updateConnectorOutboundPort);
        this.getConnectorOutboundPort = Objects.requireNonNull(getConnectorOutboundPort);
    }

    @Override
    public UpdateConnectorInboundOutput execute(UpdateConnectorInboundCommand command) {
        var aggregate = getConnectorOutboundPort.execute(command.id());
        LOG.atInfo()
                .setMessage("Starting connector update")
                .log();
        aggregate.update(command.name(), command.driverId(), command.driverName(), command.properties());
        var output = this.updateConnectorOutboundPort.execute(aggregate);
        return UpdateConnectorInboundOutput.from(output.getId(), output.getName(), output.getDriverId(),
                output.getDriverName(), output.getProperties());
    }

}