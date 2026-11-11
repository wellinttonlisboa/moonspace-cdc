package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list;

import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list.ListDriverInboundOutput;

import java.util.List;

public record ListConnectorInboundOutput(
        String id,
        String name,
        ListDriverInboundOutput driver,
        List<String> properties
) {

    public static ListConnectorInboundOutput from(String id, String name, ListDriverInboundOutput driver, List<String> properties) {
        return new ListConnectorInboundOutput(id, name, driver, properties);
    }

}