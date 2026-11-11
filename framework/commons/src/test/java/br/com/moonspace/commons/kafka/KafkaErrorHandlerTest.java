package br.com.moonspace.commons.kafka;

import br.com.moonspace.commons.support.UnitTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.listener.MessageListenerContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@UnitTest
class KafkaErrorHandlerTest {

    private final KafkaErrorHandler handler = new KafkaErrorHandler();

    @Test
    @DisplayName("skips a record that cannot be deserialized")
    void shouldSkipRecordWhenPayloadCannotBeDeserialized() {
        var partition = new TopicPartition("cdc", 1);
        var consumer = mock(Consumer.class);
        var failure = new RecordDeserializationException(partition, 4L, "bad", new IllegalStateException("bad"));

        var handled = handler.handleOne(failure, mock(ConsumerRecord.class), consumer, mock(MessageListenerContainer.class));

        assertThat(handled).isTrue();
        verify(consumer).seek(partition, 5L);
        verify(consumer).commitSync();
    }

    @Test
    @DisplayName("leaves the offset when the failure is not a deserialization error")
    void shouldLeaveOffsetWhenFailureIsNotDeserialization() {
        var consumer = mock(Consumer.class);

        handler.handleOtherException(new IllegalStateException("down"), consumer, mock(MessageListenerContainer.class), true);

        verifyNoInteractions(consumer);
    }
}
