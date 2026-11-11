package br.com.moonspace.cdc.connector.ports.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;

public interface UpdateDriverOutboundPort {
    Driver execute(Driver aggregate);
}