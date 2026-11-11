package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get;

public record GetDriverInboundQuery(
        String id
) {

    public static GetDriverInboundQuery from(String id) {
        return new GetDriverInboundQuery(id);
    }

}