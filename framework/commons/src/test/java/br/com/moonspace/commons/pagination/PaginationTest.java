package br.com.moonspace.commons.pagination;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class PaginationTest {

    @Test
    @DisplayName("builds metadata from the page counters")
    void shouldBuildMetadataWhenCreatedFromCounters() {
        var page = new Pagination<>(2, 10, 25L, List.of("a"));

        assertThat(page.meta()).isEqualTo(new Metadata(2, 10, 25L));
        assertThat(page.data()).containsExactly("a");
    }

    @Test
    @DisplayName("maps the page without changing the metadata")
    void shouldKeepMetadataWhenDataIsMapped() {
        var page = new Pagination<>(new Metadata(1, 5, 1L), List.of("10"));

        var mapped = page.map(Integer::valueOf);

        assertThat(mapped.meta()).isEqualTo(page.meta());
        assertThat(mapped.data()).containsExactly(10);
    }
}
