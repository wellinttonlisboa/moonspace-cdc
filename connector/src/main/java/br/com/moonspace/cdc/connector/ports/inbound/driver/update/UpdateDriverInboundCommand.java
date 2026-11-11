package br.com.moonspace.cdc.connector.ports.inbound.driver.update;

public record UpdateDriverInboundCommand(
        String id,
        String name
) {

}