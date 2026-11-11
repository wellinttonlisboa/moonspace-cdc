package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list;

public record ListDriverInboundOutput(
        String id,
        String name
) {

    public static ListDriverInboundOutput from(String id, String name) {
        return new ListDriverInboundOutput(id, name);
    }

}