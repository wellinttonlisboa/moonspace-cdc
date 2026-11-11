package br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver;

import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.PageableDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.core.usecases.driver.PageableDriverUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PageableDriverUseCaseConfig {

    @Bean
    public PageableDriverUseCase pageableDriverUseCase(PageableDriverOutboundAdapter pageableDriverOutboundAdapter) {
        return new PageableDriverUseCase(pageableDriverOutboundAdapter);
    }

}