package br.com.moonspace.cdc.connector.ports.outbound.queue;

import br.com.moonspace.cdc.connector.core.domain.events.connector.ConnectorEvent;

public interface ProduceConnectorOutboundPort {
    void execute(ConnectorEvent event);
}