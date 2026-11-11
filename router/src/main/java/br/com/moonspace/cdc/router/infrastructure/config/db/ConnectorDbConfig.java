package br.com.moonspace.cdc.router.infrastructure.config.db;

import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorDbRepository;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorInMemoryRepository;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorRepository;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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