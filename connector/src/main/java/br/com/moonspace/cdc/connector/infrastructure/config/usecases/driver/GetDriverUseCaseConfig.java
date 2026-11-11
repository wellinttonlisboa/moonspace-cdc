package br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver;

import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.GetDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.driver.GetDriverUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetDriverUseCaseConfig {

    @Bean
    public GetDriverUseCase getDriverUseCase(GetDriverOutboundAdapter getDriverOutboundAdapter) {
        return new GetDriverUseCase(getDriverOutboundAdapter);
    }

}