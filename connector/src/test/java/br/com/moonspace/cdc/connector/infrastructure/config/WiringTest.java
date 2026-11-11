package br.com.moonspace.cdc.connector.infrastructure.config;

import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.CreateConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.DeleteConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.GetConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.ListConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.PageableConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.connector.UpdateConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.CreateDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.DeleteDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.GetDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.ListDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.PageableDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.db.driver.UpdateDriverOutboundAdapter;
import br.com.moonspace.cdc.connector.adapters.outbound.queue.connector.ProduceConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.infrastructure.config.db.ConnectorDbConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.db.DriverDbConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector.CreateConnectorUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector.DeleteConnectorUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector.GetConnectorUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector.ListConnectorUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector.PageableConnectorUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.connector.UpdateConnectorUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver.CreateDriverUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver.DeleteDriverUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver.GetDriverUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver.ListDriverUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver.PageableDriverUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.config.usecases.driver.UpdateDriverUseCaseConfig;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepository;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@Tag("unit")
class WiringTest {

    @Test
    @DisplayName("builds every connector bean without starting Spring")
    void shouldBuildBeansWhenConfigsAreCalled() {
        var create = mock(CreateConnectorOutboundAdapter.class);
        var update = mock(UpdateConnectorOutboundAdapter.class);
        var delete = mock(DeleteConnectorOutboundAdapter.class);
        var get = mock(GetConnectorOutboundAdapter.class);
        var list = mock(ListConnectorOutboundAdapter.class);
        var page = mock(PageableConnectorOutboundAdapter.class);
        var produce = mock(ProduceConnectorOutboundAdapter.class);
        var drivers = mock(GetDriverOutboundPort.class);

        assertThat(new CreateConnectorUseCaseConfig().createConnectorUseCase(create, drivers, produce)).isNotNull();
        assertThat(new UpdateConnectorUseCaseConfig().updateConnectorUseCase(update, get, produce)).isNotNull();
        assertThat(new DeleteConnectorUseCaseConfig().deleteConnectorUseCase(delete, get, produce)).isNotNull();
        assertThat(new GetConnectorUseCaseConfig().getConnectorUseCase(get)).isNotNull();
        assertThat(new ListConnectorUseCaseConfig().listConnectorUseCase(list)).isNotNull();
        assertThat(new PageableConnectorUseCaseConfig().pageableConnectorUseCase(page)).isNotNull();

        assertThat(new CreateDriverUseCaseConfig().createDriverUseCase(mock(CreateDriverOutboundAdapter.class))).isNotNull();
        assertThat(new UpdateDriverUseCaseConfig().updateDriverUseCase(mock(UpdateDriverOutboundAdapter.class), mock(GetDriverOutboundAdapter.class))).isNotNull();
        assertThat(new DeleteDriverUseCaseConfig().deleteDriverUseCase(mock(DeleteDriverOutboundAdapter.class), mock(GetDriverOutboundAdapter.class))).isNotNull();
        assertThat(new GetDriverUseCaseConfig().getDriverUseCase(mock(GetDriverOutboundAdapter.class))).isNotNull();
        assertThat(new ListDriverUseCaseConfig().listDriverUseCase(mock(ListDriverOutboundAdapter.class))).isNotNull();
        assertThat(new PageableDriverUseCaseConfig().pageableDriverUseCase(mock(PageableDriverOutboundAdapter.class))).isNotNull();

        assertThat(new ConnectorDbConfig().connectorDatabaseRepository(mock(ConnectorRepository.class))).isNotNull();
        assertThat(new ConnectorDbConfig().connectorDatabaseInMemoryRepository()).isNotNull();
        assertThat(new DriverDbConfig().driverDatabaseRepository(mock(DriverRepository.class))).isNotNull();
        assertThat(new DriverDbConfig().driverDatabaseInMemoryRepository()).isNotNull();
        assertThat(new OpenAPIConfig().customizeOpenAPI().getComponents().getSecuritySchemes()).containsKey("bearerAuth");
    }

    @Test
    @DisplayName("logs the runtime coordinates when the application is ready")
    void shouldLogCoordinatesWhenApplicationIsReady() {
        var listener = new ReadyEventListener();
        ReflectionTestUtils.setField(listener, "environment", "test");
        ReflectionTestUtils.setField(listener, "redisHost", "localhost");
        ReflectionTestUtils.setField(listener, "redisPort", "6379");
        ReflectionTestUtils.setField(listener, "redisDatabase", "0");
        ReflectionTestUtils.setField(listener, "kafkaProducerServer", "localhost:9092");
        ReflectionTestUtils.setField(listener, "kafkaConsumerServer", "localhost:9092");

        listener.start(null);
    }
}
