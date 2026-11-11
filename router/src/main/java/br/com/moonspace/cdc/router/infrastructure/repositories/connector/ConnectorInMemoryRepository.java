package br.com.moonspace.cdc.router.infrastructure.repositories.connector;

import br.com.moonspace.cdc.router.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.commons.pagination.Pagination;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.validation.Error;
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
    public ConnectorEntity findById(String id) {
        var document = Optional.ofNullable(this.database.get(id));
        return document.orElseThrow(() -> DomainException.of(new Error("Aggregate not found")));
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
    public void deleteById(String id) {
        this.database.remove(id);
    }

}