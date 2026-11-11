package br.com.moonspace.cdc.router.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.router.adapters.outbound.db.CreateConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.core.usecases.connector.CreateConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateConnectorUseCaseConfig {

    @Bean
    public CreateConnectorUseCase createConnectorUseCase(CreateConnectorOutboundAdapter createConnectorOutboundAdapter) {
        return new CreateConnectorUseCase(createConnectorOutboundAdapter);
    }

}