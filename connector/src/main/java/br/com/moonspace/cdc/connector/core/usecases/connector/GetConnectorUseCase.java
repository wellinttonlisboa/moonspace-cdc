package br.com.moonspace.cdc.connector.core.usecases.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundQuery;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.GetConnectorOutboundPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class GetConnectorUseCase implements GetConnectorCoreApi {

    private static final Logger LOG = LoggerFactory.getLogger(GetConnectorUseCase.class);

    private final GetConnectorOutboundPort getConnectorOutboundPort;

    public GetConnectorUseCase(GetConnectorOutboundPort getConnectorOutboundPort) {
        this.getConnectorOutboundPort = Objects.requireNonNull(getConnectorOutboundPort);
    }

    @Override
    public GetConnectorInboundOutput execute(GetConnectorInboundQuery query) {
        LOG.atInfo()
                .setMessage("Searching connector Identify '{}'")
                .addArgument(query.id())
                .log();
        var output = getConnectorOutboundPort.execute(ConnectorID.from(query.id()));
        LOG.atInfo()
                .setMessage("Successfully found connector")
                .log();
        var driver = GetDriverInboundOutput.from(output.getDriver().getId().getValue(),
                output.getDriver().getName());
        return GetConnectorInboundOutput.from(output.getId().getValue(), output.getName(), driver,
                output.getProperties());
    }

}