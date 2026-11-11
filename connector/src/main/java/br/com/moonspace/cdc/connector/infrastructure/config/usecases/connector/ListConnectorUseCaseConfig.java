package br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.ListConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.connector.ListConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListConnectorUseCaseConfig {

    @Bean
    public ListConnectorUseCase listConnectorUseCase(ListConnectorOutboundAdapter listConnectorOutboundAdapter) {
        return new ListConnectorUseCase(listConnectorOutboundAdapter);
    }

}