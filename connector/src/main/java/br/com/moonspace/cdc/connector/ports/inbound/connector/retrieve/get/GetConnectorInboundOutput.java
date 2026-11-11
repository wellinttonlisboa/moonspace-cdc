package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get;

import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundOutput;

import java.util.List;

public record GetConnectorInboundOutput(
        String id,
        String name,
        GetDriverInboundOutput driver,
        List<String> properties
) {

    public static GetConnectorInboundOutput from(String id, String name, GetDriverInboundOutput driver,
                                                 List<String> properties) {
        return new GetConnectorInboundOutput(id, name, driver, properties);
    }

}