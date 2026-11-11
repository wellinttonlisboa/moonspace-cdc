package br.com.moonspace.cdc.connector.core.domain.entities.connector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("unit")
class ConnectorIDTest {

    @Test
    @DisplayName("compares connector identifiers by value")
    void shouldCompareByValueWhenIdentifiersAreCreated() {
        var generated = ConnectorID.unique();
        var id = ConnectorID.from("c-1");

        assertThat(generated.getValue()).hasSize(32);
        assertThat(id).isEqualTo(id).isEqualTo(ConnectorID.from("c-1"));
        assertThat(id.hashCode()).isEqualTo(ConnectorID.from("c-1").hashCode());
        assertThat(id).isNotEqualTo(null).isNotEqualTo("c-1").isNotEqualTo(ConnectorID.from("other"));
    }

    @Test
    @DisplayName("rejects a missing identifier")
    void shouldRejectIdentifierWhenItIsNull() {
        assertThatThrownBy(() -> ConnectorID.from(null)).isInstanceOf(NullPointerException.class);
    }
}
