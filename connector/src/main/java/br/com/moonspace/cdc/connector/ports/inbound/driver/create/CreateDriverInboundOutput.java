package br.com.moonspace.cdc.connector.ports.inbound.driver.create;

public record CreateDriverInboundOutput(
        String id,
        String name
) {

    public static CreateDriverInboundOutput from(String id,
                                                 String name) {
        return new CreateDriverInboundOutput(id,
                name
        );
    }

}