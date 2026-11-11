package br.com.moonspace.cdc.router.core.usecases.connector;

import br.com.moonspace.cdc.router.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.router.ports.inbound.connector.create.CreateConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.router.ports.inbound.connector.create.CreateConnectorInboundOutput;
import br.com.moonspace.cdc.router.ports.outbound.db.connector.CreateConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CreateConnectorUseCase implements CreateConnectorCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(CreateConnectorUseCase.class);

    private final CreateConnectorOutboundPort createConnectorOutboundPort;

    public CreateConnectorUseCase(CreateConnectorOutboundPort createConnectorOutboundPort) {
        this.createConnectorOutboundPort = Objects.requireNonNull(createConnectorOutboundPort);
    }

    @Override
    public CreateConnectorInboundOutput execute(CreateConnectorInboundCommand command) {
        LOG.atInfo()
                .setMessage("Starting connector creation")
                .log();
        var aggregate = Connector.create(command.id(), command.name(), command.driverId(),
                command.driverName(), command.properties());
        var output = this.createConnectorOutboundPort.execute(aggregate);
        return CreateConnectorInboundOutput.from(output.getId(), output.getName(), output.getDriverId(),
                output.getDriverName(), output.getProperties());
    }

}