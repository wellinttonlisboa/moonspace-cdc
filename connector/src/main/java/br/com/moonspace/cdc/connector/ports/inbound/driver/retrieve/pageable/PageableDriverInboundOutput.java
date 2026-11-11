package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable;

public record PageableDriverInboundOutput(
        String id,
        String name
) {

    public static PageableDriverInboundOutput from(String id, String name) {
        return new PageableDriverInboundOutput(id, name);
    }

}