package br.com.moonspace.cdc.connector.adapters.inbound.api.driver;

import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.delete.DeleteDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list.ListDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundOutput;
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
class DriverInboundAdapterTest {

    @Mock private CreateDriverCoreApi createApi;
    @Mock private UpdateDriverCoreApi updateApi;
    @Mock private DeleteDriverCoreApi deleteApi;
    @Mock private GetDriverCoreApi getApi;
    @Mock private ListDriverCoreApi listApi;
    @Mock private PageableDriverCoreApi pageApi;

    @Test
    @DisplayName("delegates each driver request to its use case")
    void shouldDelegateWhenARequestArrives() {
        when(createApi.execute(any())).thenReturn(CreateDriverInboundOutput.from("1", "postgres"));
        when(updateApi.execute(any())).thenReturn(UpdateDriverInboundOutput.from("1", "mysql"));
        when(getApi.execute(any())).thenReturn(GetDriverInboundOutput.from("1", "postgres"));
        when(listApi.execute()).thenReturn(List.of());
        when(pageApi.execute(any())).thenReturn(new Pagination<>(0, 10, 1, List.of(PageableDriverInboundOutput.from("1", "postgres"))));

        var created = new CreateDriverInboundAdapter(createApi).execute(new CreateDriverInboundCommand("postgres"));
        var updated = new UpdateDriverInboundAdapter(updateApi).execute(new UpdateDriverInboundCommand("1", "mysql"));
        new DeleteDriverInboundAdapter(deleteApi).execute("1");
        var found = new GetDriverInboundAdapter(getApi).execute("1");
        var listed = new ListDriverInboundAdapter(listApi).execute();
        var page = new PageableDriverInboundAdapter(pageApi).execute("postgres", 0, 10, "name", "asc");

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(updated.getHeaders().getLocation()).hasToString("/http/v1/drivers/1");
        assertThat(found.getBody().name()).isEqualTo("postgres");
        assertThat(listed.getBody()).isEmpty();
        assertThat(page.getBody().data()).hasSize(1);
        verify(deleteApi).execute(any());
    }

    @Test
    @DisplayName("rejects an adapter without its use case")
    void shouldRejectAdapterWhenUseCaseIsMissing() {
        assertThatThrownBy(() -> new CreateDriverInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateDriverInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteDriverInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new GetDriverInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ListDriverInboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new PageableDriverInboundAdapter(null)).isInstanceOf(NullPointerException.class);
    }
}
