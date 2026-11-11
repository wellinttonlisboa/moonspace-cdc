package br.com.moonspace.cdc.connector.core.domain.entities.driver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class DriverIDTest {

    @Test
    @DisplayName("builds a unique identifier and compares by value")
    void shouldCompareByValueWhenIdentifiersAreCreated() {
        var generated = DriverID.unique();
        var same = DriverID.from("driver-1");

        assertThat(generated.getValue()).hasSize(32);
        assertThat(same).isEqualTo(DriverID.from("driver-1"));
        assertThat(same).isEqualTo(same);
        assertThat(same.hashCode()).isEqualTo(DriverID.from("driver-1").hashCode());
        assertThat(same).isNotEqualTo(null);
        assertThat(same).isNotEqualTo("driver-1");
        assertThat(same).isNotEqualTo(DriverID.from("other"));
    }

    @Test
    @DisplayName("rejects a missing or empty identifier")
    void shouldRejectIdentifierWhenItIsBlank() {
        assertThatThrownBy(() -> DriverID.from(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> DriverID.from("")).isInstanceOf(IllegalArgumentException.class);
    }
}
