package br.com.moonspace.cdc.connector.infrastructure.config.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverDbRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverInMemoryRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepositoryGateway;

@Configuration
public class DriverDbConfig {

    @Bean
    @Primary
    public DriverRepositoryGateway driverDatabaseRepository(DriverRepository repository) {
        return new DriverDbRepository(repository);
    }

    @Bean
    public DriverRepositoryGateway driverDatabaseInMemoryRepository() {
        return new DriverInMemoryRepository();
    }

}