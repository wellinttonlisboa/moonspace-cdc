package br.com.moonspace.cdc.router.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.router.adapters.outbound.db.GetConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.adapters.outbound.db.UpdateConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.core.usecases.connector.UpdateConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateConnectorUseCaseConfig {

    @Bean
    public UpdateConnectorUseCase updateConnectorUseCase(UpdateConnectorOutboundAdapter updateConnectorOutboundAdapter,
                                                         GetConnectorOutboundAdapter getConnectorOutboundAdapter) {
        return new UpdateConnectorUseCase(updateConnectorOutboundAdapter, getConnectorOutboundAdapter);
    }

}