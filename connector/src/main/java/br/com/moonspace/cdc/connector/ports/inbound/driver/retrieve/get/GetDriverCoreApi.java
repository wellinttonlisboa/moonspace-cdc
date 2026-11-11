package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get;

public interface GetDriverCoreApi {
    GetDriverInboundOutput execute(GetDriverInboundQuery query);
}