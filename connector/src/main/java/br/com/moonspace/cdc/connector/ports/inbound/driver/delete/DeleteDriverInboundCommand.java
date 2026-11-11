package br.com.moonspace.cdc.connector.ports.inbound.driver.delete;

public record DeleteDriverInboundCommand(
        String id
) {

    public static DeleteDriverInboundCommand from(String id) {
        return new DeleteDriverInboundCommand(id);
    }

}