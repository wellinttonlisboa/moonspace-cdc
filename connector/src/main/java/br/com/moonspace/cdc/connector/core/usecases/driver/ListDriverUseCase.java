package br.com.moonspace.cdc.connector.core.usecases.driver;

import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list.ListDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list.ListDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.ListDriverOutboundPort;

import java.util.List;
import java.util.Objects;

public class ListDriverUseCase implements ListDriverCoreApi {

    private final ListDriverOutboundPort listDriverOutboundPort;

    public ListDriverUseCase(ListDriverOutboundPort listDriverOutboundPort) {
        this.listDriverOutboundPort = Objects.requireNonNull(listDriverOutboundPort);
    }

    @Override
    public List<ListDriverInboundOutput> execute() {
        var aggregates = listDriverOutboundPort.execute();
        return aggregates.stream()
                .map(aggregate -> ListDriverInboundOutput.from(aggregate.getId().getValue(), aggregate.getName()))
                .toList();
    }

}