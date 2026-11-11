package br.com.moonspace.commons.kafka.models.connect;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class OperationTest {

    @Test
    @DisplayName("reads an operation code ignoring case")
    void shouldResolveOperationWhenCodeMatches() {
        assertThat(Operation.of("C")).isEqualTo(Operation.CREATE);
        assertThat(Operation.of("u")).isEqualTo(Operation.UPDATE);
        assertThat(Operation.DELETE.op()).isEqualTo("d");
    }

    @Test
    @DisplayName("returns nothing when the code is unknown")
    void shouldReturnNothingWhenCodeIsUnknown() {
        assertThat(Operation.of("x")).isNull();
    }

    @Test
    @DisplayName("recognizes only the delete operation")
    void shouldRecognizeDeleteWhenOperationIsDelete() {
        assertThat(Operation.DELETE.isDelete()).isTrue();
        assertThat(Operation.isDelete(Operation.CREATE)).isFalse();
        assertThat(Operation.isDelete(null)).isFalse();
    }
}
