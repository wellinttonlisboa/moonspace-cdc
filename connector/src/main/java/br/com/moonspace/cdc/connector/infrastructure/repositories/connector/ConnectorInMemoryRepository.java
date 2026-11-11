package br.com.moonspace.cdc.connector.infrastructure.repositories.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.commons.pagination.Pagination;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotFoundException;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ConnectorInMemoryRepository implements ConnectorRepositoryGateway {

    private final Map<String, ConnectorEntity> database;

    public ConnectorInMemoryRepository() {
        this.database = Maps.newConcurrentMap();
    }

    @Override
    public ConnectorEntity save(ConnectorEntity entity) {
        this.database.put(entity.id(), entity);
        return entity;
    }

    @Override
    public ConnectorEntity findById(ConnectorID id) {
        var document = Optional.ofNullable(this.database.get(id.getValue()));
        return document.orElseThrow(() -> NotFoundException.of(Connector.class, id));
    }

    @Override
    public List<ConnectorEntity> findAll() {
        return this.database.values().stream().toList();
    }

    @Override
    public Pagination<ConnectorEntity> findAll(Pageable pageable) {
        return new Pagination<>(
                pageable.getPageNumber(),
                (int)pageable.getOffset(),
                this.database.values().size(),
                this.database.values().stream().toList()
        );
    }

    @Override
    public void deleteById(ConnectorID id) {
        this.database.remove(id.getValue());
    }

}