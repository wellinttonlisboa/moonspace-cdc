package br.com.moonspace.commons.configurations;

import br.com.moonspace.commons.kafka.KafkaErrorHandler;
import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@UnitTest
class KafkaConfigTest {

    @Test
    @DisplayName("wires the listener factory with the shared error handler")
    void shouldWireListenerWhenFactoriesAreRequested() {
        var config = new KafkaConfig();
        var handler = config.kafkaErrorHandler();
        @SuppressWarnings("unchecked")
        ConsumerFactory<String, String> consumers = mock(ConsumerFactory.class);

        var factory = config.kafkaListenerContainerFactory(consumers, handler);

        assertThat(config.jsonConverter()).isInstanceOf(StringJsonMessageConverter.class);
        assertThat(handler).isInstanceOf(KafkaErrorHandler.class);
        assertThat(factory.getConsumerFactory()).isSameAs(consumers);
    }
}
