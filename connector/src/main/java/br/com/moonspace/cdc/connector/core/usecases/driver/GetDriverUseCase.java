package br.com.moonspace.cdc.connector.core.usecases.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundQuery;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;

import java.util.Objects;

public class GetDriverUseCase implements GetDriverCoreApi {

    private final GetDriverOutboundPort getDriverOutboundPort;

    public GetDriverUseCase(GetDriverOutboundPort getDriverOutboundPort) {
        this.getDriverOutboundPort = Objects.requireNonNull(getDriverOutboundPort);
    }

    @Override
    public GetDriverInboundOutput execute(GetDriverInboundQuery query) {
        final var aggregate = getDriverOutboundPort.execute(DriverID.from(query.id()));
        return GetDriverInboundOutput.from(aggregate.getId().getValue(),
                aggregate.getName());
    }

}