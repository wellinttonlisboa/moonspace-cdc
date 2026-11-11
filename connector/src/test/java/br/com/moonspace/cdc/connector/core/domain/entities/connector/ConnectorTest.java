package br.com.moonspace.cdc.connector.core.domain.entities.connector;

import br.com.moonspace.cdc.connector.core.domain.entities.driver.Driver;
import br.com.moonspace.cdc.connector.core.domain.events.connector.ConnectorEvent;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.schema.registry.v1.connector.Operation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class ConnectorTest {

    @Test
    @DisplayName("creates and updates a connector that has a driver and properties")
    void shouldKeepPropertiesWhenConnectorIsValid() {
        var driver = Driver.create("postgres");
        var created = Connector.create(driver, "orders", List.of("host=localhost"));

        var updated = created.update("payments", List.of("host=db"));

        assertThat(updated.getName()).isEqualTo("payments");
        assertThat(updated.getProperties()).containsExactly("host=db");
        assertThat(updated.getDriver()).isSameAs(driver);
        var reloaded = Connector.from(created.getId(), driver, "payments", List.of("host=db"));
        assertThat(reloaded.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("rejects a connector without a usable name or properties")
    void shouldRejectConnectorWhenNameOrPropertiesAreInvalid() {
        var driver = Driver.create("postgres");

        assertThatThrownBy(() -> Connector.create(driver, null, List.of("a"))).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Connector.create(driver, "  ", List.of("a"))).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Connector.create(driver, "ab", List.of("a"))).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Connector.create(driver, "a".repeat(256), List.of("a"))).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Connector.create(driver, "orders", null)).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Connector.create(driver, "orders", List.of())).isInstanceOf(NotificationException.class);
    }

    @Test
    @DisplayName("describes the published connector event")
    void shouldDescribeEventWhenConnectorChanges() {
        var event = ConnectorEvent.from("1", "orders", "d1", "postgres", List.of("a"), Operation.CREATED);

        assertThat(event.toString()).isEqualTo("CREATED connector '1'");
        assertThat(event.occurredOn()).isNotNull();
    }
}
