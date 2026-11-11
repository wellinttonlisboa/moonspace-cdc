package br.com.moonspace.cdc.connector.ports.inbound.driver.update;

public interface UpdateDriverCoreApi {
    UpdateDriverInboundOutput execute(UpdateDriverInboundCommand command);
}