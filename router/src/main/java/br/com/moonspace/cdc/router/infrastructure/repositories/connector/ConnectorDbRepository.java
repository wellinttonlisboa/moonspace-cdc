package br.com.moonspace.cdc.router.infrastructure.repositories.connector;

import br.com.moonspace.cdc.router.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.commons.pagination.Pagination;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.validation.Error;
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
                    @CachePut(cacheNames = "connectors", key = "#entity.id")
            }
    )
    public ConnectorEntity save(ConnectorEntity entity) {
        this.repository.save(entity);
        return entity;
    }

    @Override
    @Cacheable(cacheNames = "connectors", key = "#id")
    public ConnectorEntity findById(String id) {
        var entity = this.repository.findById(id);
        return entity.orElseThrow(() -> DomainException.of(new Error("Aggregate not found")));
    }

    @Override
    @Cacheable(cacheNames = "connectors")
    public List<ConnectorEntity> findAll() {
        return this.repository.findAll();
    }

    @Override
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
                    @CacheEvict(cacheNames = "connectors", key = "#id")
            }
    )
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }

}