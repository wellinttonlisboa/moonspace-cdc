package br.com.moonspace.cdc.connector.ports.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;

import java.util.List;

public interface ListDriverOutboundPort {
    List<Driver> execute();
}