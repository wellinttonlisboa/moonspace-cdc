package br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver;

import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.CreateDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.driver.CreateDriverUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateDriverUseCaseConfig {

    @Bean
    public CreateDriverUseCase createDriverUseCase(CreateDriverOutboundAdapter createDriverOutboundAdapter) {
        return new CreateDriverUseCase(createDriverOutboundAdapter);
    }

}