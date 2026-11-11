package br.com.moonspace.cdc.router.infrastructure.repositories.connector;

import br.com.moonspace.cdc.router.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.commons.pagination.Pagination;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConnectorRepositoryGateway {

    ConnectorEntity save(ConnectorEntity entity);

    ConnectorEntity findById(String id);

    Pagination<ConnectorEntity> findAll(Pageable pageable);

    List<ConnectorEntity> findAll();

    void deleteById(String id);

}