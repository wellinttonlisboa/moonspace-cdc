package br.com.moonspace.cdc.connector.ports.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;

public interface DeleteDriverOutboundPort {
    void execute(DriverID id);
}