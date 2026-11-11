package br.com.moonspace.commons.utils;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class InstantUtilsTest {

    @Test
    @DisplayName("truncates the clock to microseconds")
    void shouldTruncateToMicrosWhenNowIsRequested() throws Exception {
        var now = InstantUtils.now();

        assertThat(now).isEqualTo(now.truncatedTo(ChronoUnit.MICROS));
        assertThat(privateConstructor().newInstance()).isNotNull();
    }

    private static Constructor<InstantUtils> privateConstructor() throws NoSuchMethodException {
        var constructor = InstantUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor;
    }
}
