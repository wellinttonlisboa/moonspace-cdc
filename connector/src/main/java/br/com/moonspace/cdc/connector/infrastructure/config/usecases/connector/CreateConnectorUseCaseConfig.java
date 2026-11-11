package br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.CreateConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.queue.connector.ProduceConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.connector.CreateConnectorUseCase;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateConnectorUseCaseConfig {

    @Bean
    public CreateConnectorUseCase createConnectorUseCase(CreateConnectorOutboundAdapter createConnectorOutboundAdapter,
                                                         GetDriverOutboundPort getDriverOutboundPort,
                                                         ProduceConnectorOutboundAdapter produceConnectorOutboundAdapter) {
        return new CreateConnectorUseCase(createConnectorOutboundAdapter, getDriverOutboundPort,
                produceConnectorOutboundAdapter);
    }

}