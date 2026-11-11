package br.com.moonspace.cdc.connector.ports.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;

public interface GetDriverOutboundPort {
    Driver execute(DriverID id);
}