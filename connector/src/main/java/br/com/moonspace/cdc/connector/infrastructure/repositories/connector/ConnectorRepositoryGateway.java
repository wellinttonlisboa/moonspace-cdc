package br.com.moonspace.cdc.connector.infrastructure.repositories.connector;

import java.util.List;

import org.springframework.data.domain.Pageable;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.commons.pagination.Pagination;

public interface ConnectorRepositoryGateway {

    ConnectorEntity save(ConnectorEntity entity);

    ConnectorEntity findById(ConnectorID id);

    Pagination<ConnectorEntity> findAll(Pageable pageable);

    List<ConnectorEntity> findAll();

    void deleteById(ConnectorID id);

}