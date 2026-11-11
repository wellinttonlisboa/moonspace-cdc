package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get;

public interface GetConnectorCoreApi {
    GetConnectorInboundOutput execute(GetConnectorInboundQuery query);
}