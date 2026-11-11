package br.com.moonspace.cdc.connector.infrastructure.repositories.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.commons.pagination.Pagination;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DriverRepositoryGateway {

    DriverEntity save(DriverEntity entity);

    DriverEntity findById(DriverID id);

    Pagination<DriverEntity> findAll(Pageable pageable);

    List<DriverEntity> findAll();

    void deleteById(DriverID id);

}