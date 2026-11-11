package br.com.moonspace.cdc.connector.core.domain.entities.driver;

import br.com.moonspace.core.architecture.basic.ddd.exceptions.NotificationException;
import br.com.moonspace.core.validation.handler.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class DriverTest {

    @Test
    @DisplayName("creates, reloads and renames a driver")
    void shouldKeepNameWhenDriverIsValid() {
        var created = Driver.create("postgres");
        var reloaded = Driver.from(created.getId(), "postgres");

        var renamed = reloaded.update("mysql");

        assertThat(renamed.getName()).isEqualTo("mysql");
        assertThat(renamed.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("rejects a missing, blank, short or long name")
    void shouldRejectNameWhenItBreaksTheRule() {
        var handler = Notification.create();
        Driver.from(DriverID.from("1"), "postgres").validate(handler);
        assertThat(handler.hasError()).isFalse();

        assertThatThrownBy(() -> Driver.create(null)).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Driver.create("   ")).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Driver.create("ab")).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Driver.create("a".repeat(256))).isInstanceOf(NotificationException.class);
        assertThatThrownBy(() -> Driver.create("ok").update("  x ")).isInstanceOf(NotificationException.class);
    }
}
