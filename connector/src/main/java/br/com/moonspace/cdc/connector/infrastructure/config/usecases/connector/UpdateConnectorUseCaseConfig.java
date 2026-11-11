package br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.GetConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.UpdateConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.queue.connector.ProduceConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.connector.UpdateConnectorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateConnectorUseCaseConfig {

    @Bean
    public UpdateConnectorUseCase updateConnectorUseCase(UpdateConnectorOutboundAdapter updateConnectorOutboundAdapter,
                                                         GetConnectorOutboundAdapter getConnectorOutboundAdapter,
                                                         ProduceConnectorOutboundAdapter produceConnectorOutboundAdapter) {
        return new UpdateConnectorUseCase(updateConnectorOutboundAdapter, getConnectorOutboundAdapter,
                produceConnectorOutboundAdapter);
    }

}