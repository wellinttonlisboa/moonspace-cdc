package br.com.moonspace.cdc.connector.core.usecases.connector;

import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list.ListConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list.ListConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list.ListDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.ListConnectorOutboundPort;

import java.util.List;
import java.util.Objects;

public class ListConnectorUseCase implements ListConnectorCoreApi {

    private final ListConnectorOutboundPort listConnectorOutboundPort;

    public ListConnectorUseCase(ListConnectorOutboundPort listConnectorOutboundPort) {
        this.listConnectorOutboundPort = Objects.requireNonNull(listConnectorOutboundPort);
    }

    @Override
    public List<ListConnectorInboundOutput> execute() {
        var outputs = listConnectorOutboundPort.execute();
        return outputs.stream()
                .map(output -> ListConnectorInboundOutput.from(output.getId().getValue(), output.getName(),
                        ListDriverInboundOutput.from(output.getDriver().getId().getValue(), output.getDriver().getName()),
                        output.getProperties()))
                .toList();
    }

}