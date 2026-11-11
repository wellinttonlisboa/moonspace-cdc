package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get;

public record GetDriverInboundOutput(
        String id,
        String name
) {

    public static GetDriverInboundOutput from(String id, String name) {
        return new GetDriverInboundOutput(id, name);
    }

}