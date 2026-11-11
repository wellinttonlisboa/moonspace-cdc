package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list;

import java.util.List;

public interface ListDriverCoreApi {
    List<ListDriverInboundOutput> execute();
}