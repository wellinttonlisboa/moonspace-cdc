package br.com.moonspace.cdc.connector.ports.inbound.driver.delete;

public interface DeleteDriverCoreApi {
    void execute(DeleteDriverInboundCommand command);
}