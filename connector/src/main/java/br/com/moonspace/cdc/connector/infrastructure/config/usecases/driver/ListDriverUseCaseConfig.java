package br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver;

import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.ListDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.driver.ListDriverUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListDriverUseCaseConfig {

    @Bean
    public ListDriverUseCase listDriverUseCase(ListDriverOutboundAdapter listDriverOutboundAdapter) {
        return new ListDriverUseCase(listDriverOutboundAdapter);
    }

}