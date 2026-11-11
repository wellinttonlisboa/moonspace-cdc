package br.com.moonspace.cdc.connector.infrastructure.repositories;

import br.com.moonspace.cdc.connector.core.domain.entities.connector.Connector;
import br.com.moonspace.cdc.connector.core.domain.entities.connector.ConnectorID;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.entities.driver.DriverID;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorDbRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorInMemoryRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.ConnectorRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.connector.entity.ConnectorEntity;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverDbRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverInMemoryRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.DriverRepository;
import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("unit")
class RepositoryTest {

    @Test
    @DisplayName("keeps connectors in memory until they are removed")
    void shouldStoreConnectorWhenMemoryIsUsed() {
        var repository = new ConnectorInMemoryRepository();
        var entity = ConnectorEntity.from("1", "orders", List.of("a"), DriverEntity.from("d", "postgres"));
        var page = PageRequest.of(0, 10);

        repository.save(entity);

        assertThat(repository.findById(ConnectorID.from("1"))).isEqualTo(entity);
        assertThat(repository.findAll()).containsExactly(entity);
        assertThat(repository.findAll(page).data()).containsExactly(entity);
        repository.deleteById(ConnectorID.from("1"));
        assertThatThrownBy(() -> repository.findById(ConnectorID.from("1"))).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("keeps drivers in memory until they are removed")
    void shouldStoreDriverWhenMemoryIsUsed() {
        var repository = new DriverInMemoryRepository();
        var entity = DriverEntity.from("d", "postgres");

        repository.save(entity);

        assertThat(repository.findById(DriverID.from("d"))).isEqualTo(entity);
        assertThat(repository.findAll()).containsExactly(entity);
        assertThat(repository.findAll(PageRequest.of(1, 5)).meta().currentPage()).isEqualTo(1);
        repository.deleteById(DriverID.from("d"));
        assertThatThrownBy(() -> repository.findById(DriverID.from("d"))).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("delegates connector persistence and reports a missing document")
    void shouldDelegateConnectorWhenDatabaseIsUsed() {
        var spring = mock(ConnectorRepository.class);
        var repository = new ConnectorDbRepository(spring);
        var entity = ConnectorEntity.from("1", "orders", List.of("a"), DriverEntity.from("d", "postgres"));
        var page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
        when(spring.findById("1")).thenReturn(Optional.of(entity));
        when(spring.findById("missing")).thenReturn(Optional.empty());
        when(spring.findAll()).thenReturn(List.of(entity));
        when(spring.findAll(org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        assertThat(repository.save(entity)).isEqualTo(entity);
        assertThat(repository.findById(ConnectorID.from("1"))).isEqualTo(entity);
        assertThat(repository.findAll()).containsExactly(entity);
        assertThat(repository.findAll(PageRequest.of(0, 10)).data()).containsExactly(entity);
        repository.deleteById(ConnectorID.from("1"));

        verify(spring).save(entity);
        verify(spring).deleteById("1");
        assertThatThrownBy(() -> repository.findById(ConnectorID.from("missing"))).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> new ConnectorDbRepository(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("delegates driver persistence and reports a missing document")
    void shouldDelegateDriverWhenDatabaseIsUsed() {
        var spring = mock(DriverRepository.class);
        var repository = new DriverDbRepository(spring);
        var entity = DriverEntity.from("d", "postgres");
        var page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
        when(spring.findById("d")).thenReturn(Optional.of(entity));
        when(spring.findById("missing")).thenReturn(Optional.empty());
        when(spring.findAll()).thenReturn(List.of(entity));
        when(spring.findAll(org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        assertThat(repository.save(entity)).isEqualTo(entity);
        assertThat(repository.findById(DriverID.from("d")).name()).isEqualTo("postgres");
        assertThat(repository.findAll()).containsExactly(entity);
        assertThat(repository.findAll(PageRequest.of(0, 10)).meta().total()).isEqualTo(1);
        repository.deleteById(DriverID.from("d"));

        verify(spring).deleteById("d");
        assertThatThrownBy(() -> repository.findById(DriverID.from("missing"))).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> new DriverDbRepository(null)).isInstanceOf(NullPointerException.class);
    }
}
