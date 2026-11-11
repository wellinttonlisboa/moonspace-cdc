package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get;

public record GetConnectorInboundQuery(
        String id
) {

    public static GetConnectorInboundQuery from(String id) {
        return new GetConnectorInboundQuery(id);
    }

}