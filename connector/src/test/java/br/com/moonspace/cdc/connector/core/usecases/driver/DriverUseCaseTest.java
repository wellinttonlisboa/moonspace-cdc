package br.com.moonspace.cdc.connector.core.usecases.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.delete.DeleteDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundQuery;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundQuery;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.CreateDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.DeleteDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.GetDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.ListDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.PageableDriverOutboundPort;
import br.com.moonspace.cdc.connector.ports.outbound.db.driver.UpdateDriverOutboundPort;
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
class DriverUseCaseTest {

    @Mock private CreateDriverOutboundPort createPort;
    @Mock private UpdateDriverOutboundPort updatePort;
    @Mock private DeleteDriverOutboundPort deletePort;
    @Mock private GetDriverOutboundPort getPort;
    @Mock private ListDriverOutboundPort listPort;
    @Mock private PageableDriverOutboundPort pagePort;

    @Test
    @DisplayName("creates a driver and returns its identifier")
    void shouldCreateDriverWhenNameIsValid() {
        var saved = Driver.create("postgres");
        when(createPort.execute(any())).thenReturn(saved);

        var output = new CreateDriverUseCase(createPort).execute(new CreateDriverInboundCommand("postgres"));

        assertThat(output.name()).isEqualTo("postgres");
        assertThat(output.id()).isEqualTo(saved.getId().getValue());
    }

    @Test
    @DisplayName("refuses to create a driver without a valid name")
    void shouldRejectDriverWhenNameIsInvalid() {
        assertThatThrownBy(() -> new CreateDriverUseCase(createPort).execute(new CreateDriverInboundCommand("x")))
                .isInstanceOf(NotificationException.class);
    }

    @Test
    @DisplayName("updates a stored driver")
    void shouldUpdateDriverWhenNameIsValid() {
        var stored = Driver.create("postgres");
        when(getPort.execute(any())).thenReturn(stored);
        when(updatePort.execute(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var output = new UpdateDriverUseCase(updatePort, getPort)
                .execute(new UpdateDriverInboundCommand(stored.getId().getValue(), "mysql"));

        assertThat(output.name()).isEqualTo("mysql");
    }

    @Test
    @DisplayName("refuses to update a driver with an invalid name")
    void shouldRejectUpdateWhenNameIsInvalid() {
        when(getPort.execute(any())).thenReturn(Driver.create("postgres"));

        assertThatThrownBy(() -> new UpdateDriverUseCase(updatePort, getPort)
                .execute(new UpdateDriverInboundCommand("1", "x")))
                .isInstanceOf(NotificationException.class);
    }

    @Test
    @DisplayName("deletes the driver that was found")
    void shouldDeleteDriverWhenItExists() {
        var stored = Driver.create("postgres");
        when(getPort.execute(any())).thenReturn(stored);

        new DeleteDriverUseCase(deletePort, getPort).execute(DeleteDriverInboundCommand.from(stored.getId().getValue()));

        verify(deletePort).execute(stored.getId());
    }

    @Test
    @DisplayName("returns one driver, the full list and a page")
    void shouldReturnDriversWhenTheyAreRequested() {
        var stored = Driver.create("postgres");
        when(getPort.execute(any())).thenReturn(stored);
        when(listPort.execute()).thenReturn(List.of(stored));
        when(pagePort.execute(any())).thenReturn(new Pagination<>(0, 10, 1, List.of(stored)));
        var query = PageableDriverInboundQuery.from(0, 10, "postgres", "name", "asc");

        assertThat(new GetDriverUseCase(getPort).execute(GetDriverInboundQuery.from(stored.getId().getValue())).name())
                .isEqualTo("postgres");
        assertThat(new ListDriverUseCase(listPort).execute()).hasSize(1);
        assertThat(new PageableDriverUseCase(pagePort).execute(query).data()).hasSize(1);
    }

    @Test
    @DisplayName("rejects a use case built without its ports")
    void shouldRejectUseCaseWhenAPortIsMissing() {
        assertThatThrownBy(() -> new CreateDriverUseCase(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateDriverUseCase(null, getPort)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateDriverUseCase(updatePort, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteDriverUseCase(null, getPort)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteDriverUseCase(deletePort, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new GetDriverUseCase(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ListDriverUseCase(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new PageableDriverUseCase(null)).isInstanceOf(NullPointerException.class);
    }
}
