package br.com.moonspace.cdc.connector.ports.inbound.driver.update;

public record UpdateDriverInboundOutput(
        String id,
        String name
) {

    public static UpdateDriverInboundOutput from(String id, String name) {
        return new UpdateDriverInboundOutput(id, name);
    }

}