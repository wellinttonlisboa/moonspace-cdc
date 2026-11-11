package br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.PageableConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.connector.PageableConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PageableConnectorUseCaseConfig {

    @Bean
    public PageableConnectorUseCase pageableConnectorUseCase(PageableConnectorOutboundAdapter pageableConnectorOutboundAdapter) {
        return new PageableConnectorUseCase(pageableConnectorOutboundAdapter);
    }

}