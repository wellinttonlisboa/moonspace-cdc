package br.com.moonspace.commons.configurations;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class CryptConfigTest {

    @Test
    @DisplayName("provides a bcrypt encoder")
    void shouldProvideEncoderWhenRequested() {
        var encoder = new CryptConfig().encoder();

        assertThat(encoder.matches("secret", encoder.encode("secret"))).isTrue();
    }
}
