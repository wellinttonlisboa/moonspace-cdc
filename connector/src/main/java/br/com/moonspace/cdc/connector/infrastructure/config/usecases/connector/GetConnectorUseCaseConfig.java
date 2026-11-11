package br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.GetConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.connector.GetConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetConnectorUseCaseConfig {

    @Bean
    public GetConnectorUseCase getConnectorUseCase(GetConnectorOutboundAdapter getConnectorOutboundAdapter) {
        return new GetConnectorUseCase(getConnectorOutboundAdapter);
    }

}