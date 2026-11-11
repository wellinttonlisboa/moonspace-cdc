package br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver;

import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.GetDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.UpdateDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.driver.UpdateDriverUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateDriverUseCaseConfig {

    @Bean
    public UpdateDriverUseCase updateDriverUseCase(UpdateDriverOutboundAdapter updateDriverOutboundAdapter,
                                                      GetDriverOutboundAdapter getDriverOutboundAdapter) {
        return new UpdateDriverUseCase(updateDriverOutboundAdapter, getDriverOutboundAdapter);
    }

}