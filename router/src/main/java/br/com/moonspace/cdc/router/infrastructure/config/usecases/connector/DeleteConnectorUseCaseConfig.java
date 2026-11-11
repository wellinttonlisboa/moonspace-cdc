package br.com.moonspace.cdc.router.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.router.adapters.outbound.db.DeleteConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.adapters.outbound.db.GetConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.core.usecases.connector.DeleteConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteConnectorUseCaseConfig {

    @Bean
    public DeleteConnectorUseCase deleteConnectorUseCase(DeleteConnectorOutboundAdapter deleteConnectorOutboundAdapter,
                                                         GetConnectorOutboundAdapter getConnectorOutboundAdapter) {
        return new DeleteConnectorUseCase(deleteConnectorOutboundAdapter, getConnectorOutboundAdapter);
    }

}