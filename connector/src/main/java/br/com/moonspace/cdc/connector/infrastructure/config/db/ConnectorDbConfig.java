package br.com.moonspace.cdc.connector.infrastructure.config.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorDbRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorInMemoryRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepositoryGateway;

@Configuration
public class ConnectorDbConfig {

    @Bean
    @Primary
    public ConnectorRepositoryGateway connectorDatabaseRepository(ConnectorRepository repository) {
        return new ConnectorDbRepository(repository);
    }

    @Bean
    public ConnectorRepositoryGateway connectorDatabaseInMemoryRepository() {
        return new ConnectorInMemoryRepository();
    }

}