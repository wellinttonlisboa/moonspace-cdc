package br.com.moonspace.cdc.router.ports.inbound.connector.delete;

public record DeleteConnectorInboundCommand(
        String id
) {

    public static DeleteConnectorInboundCommand from(String id) {
        return new DeleteConnectorInboundCommand(id);
    }

}