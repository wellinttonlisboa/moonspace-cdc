package br.com.moonspace.commons.utils;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class GenIdTest {

    @Test
    @DisplayName("builds a 32-character uppercase identifier without dashes")
    void shouldBuildUppercaseIdentifierWhenRequested() {
        var id = GenId.newId();

        assertThat(id).hasSize(32).matches("[0-9A-F]+");
        assertThat(new GenId()).isNotNull();
    }
}
