package br.com.moonspace.cdc.connector.core.usecases.connector;

import br.com.moonspace.cdc.connector.adapters.outbound.queue.connector.ProduceConnectorOutboundAdapter;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.delete.DeleteConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundQuery;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundQuery;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.CreateConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.DeleteConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.GetConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.ListConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.PageableConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.connector.UpdateConnectorOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.queue.ProduceConnectorOutboundPort;
import br.com.moonspace.commons.pagination.Pagination;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class ConnectorUseCaseTest {

    @Mock private CreateConnectorOutboundPort createPort;
    @Mock private UpdateConnectorOutboundPort updatePort;
    @Mock private DeleteConnectorOutboundPort deletePort;
    @Mock private GetConnectorOutboundPort getPort;
    @Mock private GetDriverOutboundPort getDriverPort;
    @Mock private ListConnectorOutboundPort listPort;
    @Mock private PageableConnectorOutboundPort pagePort;
    @Mock private ProduceConnectorOutboundPort producePort;
    @Mock private ProduceConnectorOutboundAdapter produceAdapter;

    @Test
    @DisplayName("creates a connector and publishes the event")
    void shouldCreateConnectorWhenCommandIsValid() {
        var driver = Driver.create("postgres");
        var saved = Connector.create(driver, "orders", List.of("host=localhost"));
        when(getDriverPort.execute(any())).thenReturn(driver);
        when(createPort.execute(any())).thenReturn(saved);

        var output = new CreateConnectorUseCase(createPort, getDriverPort, producePort)
                .execute(new CreateConnectorInboundCommand(driver.getId().getValue(), "orders", List.of("host=localhost")));

        assertThat(output.name()).isEqualTo("orders");
        verify(producePort).execute(any());
    }

    @Test
    @DisplayName("refuses to create a connector that fails validation")
    void shouldRejectConnectorWhenCommandIsInvalid() {
        when(getDriverPort.execute(any())).thenReturn(Driver.create("postgres"));

        assertThatThrownBy(() -> new CreateConnectorUseCase(createPort, getDriverPort, producePort)
                .execute(new CreateConnectorInboundCommand("1", "x", List.of())))
                .isInstanceOf(NotificationException.class);
    }

    @Test
    @DisplayName("updates a connector and publishes the event")
    void shouldUpdateConnectorWhenCommandIsValid() {
        var stored = Connector.create(Driver.create("postgres"), "orders", List.of("host=localhost"));
        when(getPort.execute(any())).thenReturn(stored);
        when(updatePort.execute(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var output = new UpdateConnectorUseCase(updatePort, getPort, produceAdapter)
                .execute(new UpdateConnectorInboundCommand(stored.getId().getValue(), "payments", List.of("host=db")));

        assertThat(output.name()).isEqualTo("payments");
        verify(produceAdapter).execute(any());
    }

    @Test
    @DisplayName("refuses to update a connector that fails validation")
    void shouldRejectUpdateWhenCommandIsInvalid() {
        when(getPort.execute(any())).thenReturn(Connector.create(Driver.create("postgres"), "orders", List.of("a")));

        assertThatThrownBy(() -> new UpdateConnectorUseCase(updatePort, getPort, produceAdapter)
                .execute(new UpdateConnectorInboundCommand("1", "x", List.of())))
                .isInstanceOf(NotificationException.class);
    }

    @Test
    @DisplayName("deletes a connector and publishes the event")
    void shouldDeleteConnectorWhenItExists() {
        var stored = Connector.create(Driver.create("postgres"), "orders", List.of("a"));
        when(getPort.execute(any())).thenReturn(stored);

        new DeleteConnectorUseCase(deletePort, getPort, producePort)
                .execute(DeleteConnectorInboundCommand.from(stored.getId().getValue()));

        verify(deletePort).execute(stored.getId());
        verify(producePort).execute(any());
    }

    @Test
    @DisplayName("returns one connector, the full list and a page")
    void shouldReturnConnectorsWhenTheyAreRequested() {
        var stored = Connector.create(Driver.create("postgres"), "orders", List.of("a"));
        when(getPort.execute(any())).thenReturn(stored);
        when(listPort.execute()).thenReturn(List.of(stored));
        when(pagePort.execute(any())).thenReturn(new Pagination<>(0, 10, 1, List.of(stored)));

        assertThat(new GetConnectorUseCase(getPort).execute(GetConnectorInboundQuery.from(stored.getId().getValue())).name())
                .isEqualTo("orders");
        assertThat(new ListConnectorUseCase(listPort).execute()).hasSize(1);
        assertThat(new PageableConnectorUseCase(pagePort)
                .execute(PageableConnectorInboundQuery.from(0, 10, "orders", "name", "asc")).data())
                .hasSize(1);
    }

    @Test
    @DisplayName("rejects a use case built without its ports")
    void shouldRejectUseCaseWhenAPortIsMissing() {
        assertThatThrownBy(() -> new CreateConnectorUseCase(null, getDriverPort, producePort)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new CreateConnectorUseCase(createPort, null, producePort)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new CreateConnectorUseCase(createPort, getDriverPort, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorUseCase(null, getPort, produceAdapter)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorUseCase(updatePort, null, produceAdapter)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorUseCase(updatePort, getPort, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorUseCase(null, getPort, producePort)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorUseCase(deletePort, null, producePort)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorUseCase(deletePort, getPort, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new GetConnectorUseCase(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ListConnectorUseCase(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new PageableConnectorUseCase(null)).isInstanceOf(NullPointerException.class);
    }
}
