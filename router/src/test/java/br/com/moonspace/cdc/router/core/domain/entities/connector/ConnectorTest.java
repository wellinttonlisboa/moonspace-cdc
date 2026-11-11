package br.com.moonspace.cdc.router.core.domain.entities.connector;

import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.validation.handler.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class ConnectorTest {

    @Test
    @DisplayName("creates and updates a connector")
    void shouldKeepIdentityWhenConnectorIsUpdated() {
        var created = Connector.create("1", "orders", "d1", "postgres", List.of("a"));

        var updated = created.update("payments", "d2", "mysql", List.of("b"));

        assertThat(updated.getId()).isEqualTo("1");
        assertThat(updated.getName()).isEqualTo("payments");
        assertThat(updated.getDriverId()).isEqualTo("d2");
        assertThat(updated.getDriverName()).isEqualTo("mysql");
        assertThat(updated.getProperties()).containsExactly("b");
        assertThat(Connector.from("1", "payments", "d2", "mysql", List.of("b")).getName()).isEqualTo("payments");
    }

    @Test
    @DisplayName("rejects a connector without an identifier or a name")
    void shouldRejectConnectorWhenIdentityIsMissing() throws Exception {
        var valid = Notification.create();
        Connector.from("1", "orders", "d", "postgres", List.of()).validate(valid);
        assertThat(valid.hasError()).isFalse();

        var invalid = Connector.from("1", "orders", "d", "postgres", List.of());
        set(invalid, "id", null);
        set(invalid, "name", "  ");
        var handler = Notification.create();
        invalid.validate(handler);
        assertThat(handler.getErrors()).hasSize(2);

        assertThatThrownBy(() -> Connector.create(null, "orders", "d", "postgres", List.of())).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> Connector.create("  ", "orders", "d", "postgres", List.of())).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> Connector.create("1", null, "d", "postgres", List.of())).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> Connector.create("1", "  ", "d", "postgres", List.of())).isInstanceOf(DomainException.class);
        assertThatThrownBy(() -> Connector.create("1", "orders", "d", "postgres", List.of()).update(null, "d", "postgres", List.of()))
                .isInstanceOf(DomainException.class);
    }

    private static void set(Connector connector, String field, Object value) throws Exception {
        Field declared = Connector.class.getDeclaredField(field);
        declared.setAccessible(true);
        declared.set(connector, value);
    }
}
