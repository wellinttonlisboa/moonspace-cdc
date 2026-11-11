package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list;

import java.util.List;

public interface ListConnectorCoreApi {
    List<ListConnectorInboundOutput> execute();
}