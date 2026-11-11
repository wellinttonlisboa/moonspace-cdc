package br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.DeleteConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.GetConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.queue.connector.ProduceConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.connector.DeleteConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteConnectorUseCaseConfig {

    @Bean
    public DeleteConnectorUseCase deleteConnectorUseCase(DeleteConnectorOutboundAdapter deleteConnectorOutboundAdapter,
                                                         GetConnectorOutboundAdapter getConnectorOutboundAdapter,
                                                         ProduceConnectorOutboundAdapter produceConnectorOutboundAdapter) {
        return new DeleteConnectorUseCase(deleteConnectorOutboundAdapter, getConnectorOutboundAdapter,
                produceConnectorOutboundAdapter);
    }

}