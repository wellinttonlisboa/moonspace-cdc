package br.com.moonspace.cdc.connector.adapters.inbound.api.connector;

import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.delete.DeleteConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list.ListConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundOutput;
import br.com.moonspace.commons.pagination.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class ConnectorInboundAdapterTest {

    @Mock private CreateConnectorCoreApi createApi;
    @Mock private UpdateConnectorCoreApi updateApi;
    @Mock private DeleteConnectorCoreApi deleteApi;
    @Mock private GetConnectorCoreApi getApi;
    @Mock private ListConnectorCoreApi listApi;
    @Mock private PageableConnectorCoreApi pageApi;

    @Test
    @DisplayName("delegates each connector request to its use case")
    void shouldDelegateWhenARequestArrives() {
        when(createApi.execute(any())).thenReturn(CreateConnectorInboundOutput.from("1", "orders", List.of("a")));
        when(updateApi.execute(any())).thenReturn(UpdateConnectorInboundOutput.from("1", "orders", List.of("a")));
        when(getApi.execute(any())).thenReturn(GetConnectorInboundOutput.from("1", "orders",
                GetDriverInboundOutput.from("d", "postgres"), List.of("a")));
        when(listApi.execute()).thenReturn(List.of());
        when(pageApi.execute(any())).thenReturn(new Pagination<>(0, 10, 0, List.of(PageableConnectorInboundOutput.from(
                "1", "orders", null, List.of("a")))));

        var created = new CreateConnectorInboundAdapter(createApi).execute(new CreateConnectorInboundCommand("d", "orders", List.of("a")));
        var updated = new UpdateConnectorInboundAdapter(updateApi).execute(new UpdateConnectorInboundCommand("1", "orders", List.of("a")));
        new DeleteConnectorInboundAdapter(deleteApi).execute("1");
        var found = new GetConnectorInboundAdapter(getApi).execute("1");
        var listed = new ListConnectorInboundAdapter(listApi).execute();
        var page = new PageableConnectorInboundAdapter(pageApi).execute("orders", 0, 10, "name", "asc");

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getHeaders().getLocation()).hasToString("/http/v1/connectors/1");
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(found.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listed.getBody()).isEmpty();
        assertThat(page.getBody().data()).hasSize(1);
        verify(deleteApi).execute(any());
    }

    @Test
    @DisplayName("rejects an adapter without its use case")
    void shouldRejectAdapterWhenUseCaseIsMissing() {
        assertThatThrownBy(() -> new CreateConnectorInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new GetConnectorInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ListConnectorInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new PageableConnectorInboundAdapter(null)).isInstanceOf(NullPointerException.class);
    }
}
