package br.com.moonspace.cdc.connector.adapters.outbound.db.driver;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepositoryGateway;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundQuery;
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
class DriverOutboundAdapterTest {

    @Mock private DriverRepositoryGateway repository;

    @Test
    @DisplayName("stores and reloads a driver")
    void shouldPersistDriverWhenItIsSaved() {
        var aggregate = Driver.create("postgres");
        var entity = DriverEntity.from(aggregate.getId().getValue(), aggregate.getName());
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(any())).thenReturn(entity);
        when(repository.findAll()).thenReturn(List.of(entity));
        when(repository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new Pagination<>(0, 10, 1, List.of(entity)));

        assertThat(new CreateDriverOutboundAdapter(repository).execute(aggregate).getName()).isEqualTo("postgres");
        assertThat(new UpdateDriverOutboundAdapter(repository).execute(aggregate).getName()).isEqualTo("postgres");
        assertThat(new GetDriverOutboundAdapter(repository).execute(aggregate.getId()).getName()).isEqualTo("postgres");
        assertThat(new ListDriverOutboundAdapter(repository).execute()).hasSize(1);
        assertThat(new PageableDriverOutboundAdapter(repository)
                .execute(PageableDriverInboundQuery.from(0, 10, "postgres", "name", "asc")).data())
                .hasSize(1);
        new DeleteDriverOutboundAdapter(repository).execute(aggregate.getId());

        verify(repository).deleteById(aggregate.getId());
    }

    @Test
    @DisplayName("rejects an adapter without a repository")
    void shouldRejectAdapterWhenRepositoryIsMissing() {
        assertThatThrownBy(() -> new CreateDriverOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new UpdateDriverOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new GetDriverOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ListDriverOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new PageableDriverOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new DeleteDriverOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
    }
}
