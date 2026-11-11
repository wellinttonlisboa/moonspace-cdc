package br.com.moonspace.cdc.connector.ports.inbound.driver.create;

public interface CreateDriverCoreApi {
    CreateDriverInboundOutput execute(CreateDriverInboundCommand command);
}