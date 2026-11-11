package br.com.moonspace.commons.utils;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class CollectionUtilsTest {

    @Test
    @DisplayName("returns an empty set when the source is missing")
    void shouldReturnEmptySetWhenSourceIsNull() {
        assertThat(CollectionUtils.mapTo(null, String::valueOf)).isEmpty();
        assertThat(CollectionUtils.nullIfEmpty(null)).isEmpty();
    }

    @Test
    @DisplayName("returns an empty set when the source has no values")
    void shouldReturnEmptySetWhenSourceIsEmpty() {
        assertThat(CollectionUtils.nullIfEmpty(Set.of())).isEmpty();
    }

    @Test
    @DisplayName("maps every value and keeps a populated set")
    void shouldMapValuesWhenSourceIsPopulated() throws Exception {
        assertThat(CollectionUtils.mapTo(Set.of(1, 2), String::valueOf)).containsExactlyInAnyOrder("1", "2");
        assertThat(CollectionUtils.nullIfEmpty(Set.of("a"))).containsExactly("a");
        var constructor = CollectionUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }
}
