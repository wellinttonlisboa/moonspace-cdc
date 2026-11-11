package br.com.moonspace.cdc.connector.infrastructure.repositories.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.commons.pagination.Pagination;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ConnectorDbRepository implements ConnectorRepositoryGateway {

    private final ConnectorRepository repository;

    public ConnectorDbRepository(ConnectorRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    @Caching(
            put = {
                    @CachePut(cacheNames = "connector", key = "#entity.id")
            }
    )
    public ConnectorEntity save(ConnectorEntity entity) {
        this.repository.save(entity);
        return entity;
    }

    @Override
    @Cacheable(cacheNames = "connector", key = "#id.value")
    public ConnectorEntity findById(ConnectorID id) {
        var entity = this.repository.findById(id.getValue());
        return entity.orElseThrow(() -> NotFoundException.of(Connector.class, id));
    }

    @Override
    @Cacheable(cacheNames = "connector")
    public List<ConnectorEntity> findAll() {
        return this.repository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "connector")
    public Pagination<ConnectorEntity> findAll(Pageable pageable) {
        var entities = this.repository.findAll(pageable);
        return new Pagination<>(
                entities.getNumber(),
                entities.getSize(),
                entities.getTotalElements(),
                entities.stream().toList()
        );
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "connector", key = "#id.value")
            }
    )
    public void deleteById(ConnectorID id) {
        this.repository.deleteById(id.getValue());
    }

}