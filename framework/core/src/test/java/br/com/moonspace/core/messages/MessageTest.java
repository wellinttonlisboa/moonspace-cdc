package br.com.moonspace.core.messages;

import br.com.moonspace.core.support.Samples.SampleCommand;
import br.com.moonspace.core.support.Samples.SampleQuery;
import br.com.moonspace.core.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class MessageTest {

    @Test
    @DisplayName("keeps the identifier of a command")
    void shouldKeepIdentifierWhenCommandIsCreated() {
        var command = new SampleCommand("cmd-1");

        assertThat(command.getId()).isEqualTo("cmd-1");
        assertThat(command).isEqualTo(new SampleCommand("cmd-1"));
        assertThat(new SampleQuery()).isNotNull();
    }
}
