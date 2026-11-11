package br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver;

import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.DeleteDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.GetDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.driver.DeleteDriverUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteDriverUseCaseConfig {

    @Bean
    public DeleteDriverUseCase deleteDriverUseCase(DeleteDriverOutboundAdapter deleteDriverOutboundAdapter,
                                                   GetDriverOutboundAdapter getDriverOutboundAdapter) {
        return new DeleteDriverUseCase(deleteDriverOutboundAdapter, getDriverOutboundAdapter);
    }

}