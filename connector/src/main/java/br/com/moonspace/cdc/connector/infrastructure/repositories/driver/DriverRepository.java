package br.com.moonspace.cdc.connector.infrastructure.repositories.driver;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;

@Repository
public interface DriverRepository extends MongoRepository<DriverEntity, String> {

}