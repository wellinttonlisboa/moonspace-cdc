package br.com.moonspace.cdc.router;

import br.com.moonspace.cdc.router.adapters.inbound.queue.connector.ConnectorInboundAdapter;
import br.com.moonspace.cdc.router.adapters.outbound.db.CreateConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.adapters.outbound.db.DeleteConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.adapters.outbound.db.GetConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.adapters.outbound.db.UpdateConnectorOutboundAdapter;
import br.com.moonspace.cdc.router.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.router.core.usecases.connector.CreateConnectorUseCase;
import br.com.moonspace.cdc.router.core.usecases.connector.DeleteConnectorUseCase;
import br.com.moonspace.cdc.router.core.usecases.connector.UpdateConnectorUseCase;
import br.com.moonspace.cdc.router.infrastructure.config.OpenAPIConfig;
import br.com.moonspace.cdc.router.infrastructure.config.ReadyEventListener;
import br.com.moonspace.cdc.router.infrastructure.config.db.ConnectorDbConfig;
import br.com.moonspace.cdc.router.infrastructure.config.usecases.connector.CreateConnectorUseCaseConfig;
import br.com.moonspace.cdc.router.infrastructure.config.usecases.connector.DeleteConnectorUseCaseConfig;
import br.com.moonspace.cdc.router.infrastructure.config.usecases.connector.UpdateConnectorUseCaseConfig;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorDbRepository;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorInMemoryRepository;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorRepository;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.router.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.cdc.router.ports.inbound.connector.create.CreateConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.router.ports.inbound.connector.delete.DeleteConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.delete.DeleteConnectorInboundCommand;
import br.com.moonspace.cdc.router.ports.inbound.connector.update.UpdateConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.update.UpdateConnectorInboundCommand;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.schema.registry.v1.connector.ConnectorRecord;
import br.com.moonspace.schema.registry.v1.connector.Operation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
class RouterBehaviorTest {

    @Test
    @DisplayName("creates, updates and deletes through the use cases")
    void shouldChangeConnectorWhenUseCaseRuns() {
        var gateway = mock(ConnectorRepositoryGateway.class);
        var stored = Connector.create("1", "orders", "d", "postgres", List.of("a"));
        when(gateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(gateway.findById("1")).thenReturn(ConnectorEntity.from("1", "orders", "d", "postgres", List.of("a")));
        var create = new CreateConnectorOutboundAdapter(gateway);
        var update = new UpdateConnectorOutboundAdapter(gateway);
        var get = new GetConnectorOutboundAdapter(gateway);
        var delete = new DeleteConnectorOutboundAdapter(gateway);

        var created = new CreateConnectorUseCase(create).execute(CreateConnectorInboundCommand.from("1", "orders", "d", "postgres", List.of("a")));
        var updated = new UpdateConnectorUseCase(update, get).execute(UpdateConnectorInboundCommand.from("1", "payments", "d2", "mysql", List.of("b")));
        new DeleteConnectorUseCase(delete, get).execute(DeleteConnectorInboundCommand.from("1"));

        assertThat(created.name()).isEqualTo("orders");
        assertThat(updated.name()).isEqualTo("payments");
        verify(gateway).deleteById("1");
        assertThat(stored.getId()).isEqualTo("1");
        assertThatThrownBy(() -> new CreateConnectorUseCase(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorUseCase(null, get)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorUseCase(update, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorUseCase(null, get)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorUseCase(delete, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new CreateConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new GetConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("routes created, updated and deleted events")
    void shouldRouteEventWhenARecordArrives() {
        var create = mock(CreateConnectorCoreApi.class);
        var update = mock(UpdateConnectorCoreApi.class);
        var delete = mock(DeleteConnectorCoreApi.class);
        var adapter = new ConnectorInboundAdapter(create, update, delete);

        adapter.on(record(Operation.CREATED));
        adapter.on(record(Operation.UPDATED));
        adapter.on(record(Operation.DELETED));

        verify(create).execute(any());
        verify(update).execute(any());
        verify(delete).execute(any());
        assertThatThrownBy(() -> new ConnectorInboundAdapter(null, update, delete)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ConnectorInboundAdapter(create, null, delete)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ConnectorInboundAdapter(create, update, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("stores connectors in memory and through the database gateway")
    void shouldStoreConnectorWhenRepositoryIsUsed() {
        var memory = new ConnectorInMemoryRepository();
        var entity = ConnectorEntity.from("1", "orders", "d", "postgres", List.of("a"));
        memory.save(entity);
        assertThat(memory.findById("1")).isEqualTo(entity);
        assertThat(memory.findAll()).containsExactly(entity);
        assertThat(memory.findAll(PageRequest.of(0, 5)).data()).containsExactly(entity);
        memory.deleteById("1");
        assertThatThrownBy(() -> memory.findById("1")).isInstanceOf(DomainException.class);

        var spring = mock(ConnectorRepository.class);
        var database = new ConnectorDbRepository(spring);
        when(spring.findById("1")).thenReturn(java.util.Optional.of(entity));
        when(spring.findById("missing")).thenReturn(java.util.Optional.empty());
        when(spring.findAll()).thenReturn(List.of(entity));
        when(spring.findAll(org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(entity)));
        assertThat(database.save(entity)).isEqualTo(entity);
        assertThat(database.findById("1")).isEqualTo(entity);
        assertThat(database.findAll()).containsExactly(entity);
        assertThat(database.findAll(PageRequest.of(0, 10)).meta().total()).isEqualTo(1);
        database.deleteById("1");
        verify(spring).deleteById("1");
        assertThatThrownBy(() -> database.findById("missing")).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> new ConnectorDbRepository(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("builds the router beans and announces readiness")
    void shouldBuildBeansWhenConfigsAreCalled() {
        var create = mock(CreateConnectorOutboundAdapter.class);
        var update = mock(UpdateConnectorOutboundAdapter.class);
        var delete = mock(DeleteConnectorOutboundAdapter.class);
        var get = mock(GetConnectorOutboundAdapter.class);
        assertThat(new CreateConnectorUseCaseConfig().createConnectorUseCase(create)).isNotNull();
        assertThat(new UpdateConnectorUseCaseConfig().updateConnectorUseCase(update, get)).isNotNull();
        assertThat(new DeleteConnectorUseCaseConfig().deleteConnectorUseCase(delete, get)).isNotNull();
        assertThat(new ConnectorDbConfig().connectorDatabaseRepository(mock(ConnectorRepository.class))).isNotNull();
        assertThat(new ConnectorDbConfig().connectorDatabaseInMemoryRepository()).isNotNull();
        assertThat(new OpenAPIConfig().customizeOpenAPI().getComponents().getSecuritySchemes()).containsKey("bearerAuth");

        var listener = new ReadyEventListener();
        ReflectionTestUtils.setField(listener, "environment", "test");
        ReflectionTestUtils.setField(listener, "redisHost", "localhost");
        ReflectionTestUtils.setField(listener, "redisPort", "6379");
        ReflectionTestUtils.setField(listener, "redisDatabase", "0");
        ReflectionTestUtils.setField(listener, "kafkaProducerServer", "localhost:9092");
        ReflectionTestUtils.setField(listener, "kafkaConsumerServer", "localhost:9092");
        listener.start(null);
    }

    private static ConsumerRecord<String, ConnectorRecord> record(Operation operation) {
        var payload = new ConnectorRecord("1", "orders", "d", "postgres", List.of("a"), operation, Instant.now());
        return new ConsumerRecord<>("topic", 0, 0L, "key", payload);
    }
}
