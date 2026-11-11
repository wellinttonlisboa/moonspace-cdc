package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable;

import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundOutput;

import java.util.List;

public record PageableConnectorInboundOutput(
        String id,
        String name,
        PageableDriverInboundOutput driver,
        List<String> properties
) {

    public static PageableConnectorInboundOutput from(String id, String name, PageableDriverInboundOutput driver,
                                                      List<String> properties) {
        return new PageableConnectorInboundOutput(id, name, driver, properties);
    }

}