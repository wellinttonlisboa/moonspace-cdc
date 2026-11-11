package br.com.moonspace.cdc.connector.adapters.outbound.db.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepositoryGateway;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundQuery;
import br.com.moonspace.commons.pagination.Pagination;
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
class ConnectorOutboundAdapterTest {

    @Mock private ConnectorRepositoryGateway repository;

    @Test
    @DisplayName("stores and reloads a connector")
    void shouldPersistConnectorWhenItIsSaved() {
        var aggregate = Connector.create(Driver.create("postgres"), "orders", List.of("host=localhost"));
        var entity = ConnectorEntity.from(aggregate.getId().getValue(), aggregate.getName(), aggregate.getProperties(),
                DriverEntity.from(aggregate.getDriver().getId().getValue(), aggregate.getDriver().getName()));
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(any())).thenReturn(entity);
        when(repository.findAll()).thenReturn(List.of(entity));
        when(repository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new Pagination<>(0, 10, 1, List.of(entity)));

        assertThat(new CreateConnectorOutboundAdapter(repository).execute(aggregate).getName()).isEqualTo("orders");
        assertThat(new UpdateConnectorOutboundAdapter(repository).execute(aggregate).getName()).isEqualTo("orders");
        assertThat(new GetConnectorOutboundAdapter(repository).execute(aggregate.getId()).getName()).isEqualTo("orders");
        assertThat(new ListConnectorOutboundAdapter(repository).execute()).hasSize(1);
        assertThat(new PageableConnectorOutboundAdapter(repository)
                .execute(PageableConnectorInboundQuery.from(0, 10, "orders", "name", "asc")).data())
                .hasSize(1);
        new DeleteConnectorOutboundAdapter(repository).execute(aggregate.getId());

        verify(repository).deleteById(aggregate.getId());
    }

    @Test
    @DisplayName("rejects an adapter without a repository")
    void shouldRejectAdapterWhenRepositoryIsMissing() {
        assertThatThrownBy(() -> new CreateConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new GetConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ListConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new PageableConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
    }
}
