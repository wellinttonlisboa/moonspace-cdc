package br.com.moonspace.core.validation;

import br.com.moonspace.core.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class ErrorTest {

    @Test
    @DisplayName("builds an error from its message")
    void shouldCreateErrorWhenMessageIsProvided() {
        var error = Error.of("name is required");

        assertThat(error.error()).isEqualTo("name is required");
    }
}
