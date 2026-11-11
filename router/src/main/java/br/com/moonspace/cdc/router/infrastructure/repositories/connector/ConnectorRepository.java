package br.com.moonspace.cdc.router.infrastructure.repositories.connector;

import br.com.moonspace.cdc.router.infrastructure.repositories.connector.entity.ConnectorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectorRepository extends MongoRepository<ConnectorEntity, String> {

}