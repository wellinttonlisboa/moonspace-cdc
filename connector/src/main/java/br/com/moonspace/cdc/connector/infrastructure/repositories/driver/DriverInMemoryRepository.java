package br.com.moonspace.cdc.connector.infrastructure.repositories.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.commons.pagination.Pagination;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotFoundException;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DriverInMemoryRepository implements DriverRepositoryGateway {

    private final Map<String, DriverEntity> database;

    public DriverInMemoryRepository() {
        this.database = Maps.newConcurrentMap();
    }

    @Override
    public DriverEntity save(DriverEntity entity) {
        this.database.put(entity.id(), entity);
        return entity;
    }

    @Override
    public DriverEntity findById(DriverID id) {
        var document = Optional.ofNullable(this.database.get(id.getValue()));
        return document.orElseThrow(() -> NotFoundException.of(Driver.class, id));
    }

    @Override
    public List<DriverEntity> findAll() {
        return this.database.values().stream().toList();
    }

    @Override
    public Pagination<DriverEntity> findAll(Pageable pageable) {
        return new Pagination<>(
                pageable.getPageNumber(),
                (int)pageable.getOffset(),
                this.database.values().size(),
                this.database.values().stream().toList()
        );
    }

    @Override
    public void deleteById(DriverID id) {
        this.database.remove(id.getValue());
    }

}