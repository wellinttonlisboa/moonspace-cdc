package br.com.moonspace.cdc.connector.infrastructure.repositories.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
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
public class DriverDbRepository implements DriverRepositoryGateway {

    private final DriverRepository repository;

    public DriverDbRepository(DriverRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    @Caching(
            put = {
                    @CachePut(cacheNames = "driver", key = "#entity.id")
            }
    )
    public DriverEntity save(DriverEntity entity) {
        this.repository.save(entity);
        return entity;
    }

    @Override
    @Cacheable(cacheNames = "driver", key = "#id.value")
    public DriverEntity findById(DriverID id) {
        var entity = this.repository.findById(id.getValue());
        return entity.orElseThrow(() -> NotFoundException.of(Driver.class, id));
    }

    @Override
    @Cacheable(cacheNames = "driver")
    public List<DriverEntity> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Pagination<DriverEntity> findAll(Pageable pageable) {
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
                    @CacheEvict(cacheNames = "driver", key = "#id.value")
            }
    )
    public void deleteById(DriverID id) {
        this.repository.deleteById(id.getValue());
    }

}