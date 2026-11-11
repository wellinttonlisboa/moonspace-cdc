package br.com.moonspace.cdc.connector.adapters.outbound.queue.connector;

import br.com.moonspace.cdc.connector.core.domain.events.connector.ConnectorEvent;
import br.com.moonspace.schema.registry.v1.connector.ConnectorRecord;
import br.com.moonspace.schema.registry.v1.connector.Operation;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
class ProduceConnectorOutboundAdapterTest {

    @Test
    @DisplayName("logs success and failure after the broker answers")
    void shouldLogBothOutcomesWhenTheBrokerAnswers() {
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, ConnectorRecord> producer = mock(KafkaTemplate.class);
        var adapter = new ProduceConnectorOutboundAdapter(producer);
        ReflectionTestUtils.setField(adapter, "topic", "MOONSPACE-CDC-Connector");
        var success = new CompletableFuture<SendResult<String, ConnectorRecord>>();
        var failure = new CompletableFuture<SendResult<String, ConnectorRecord>>();
        when(producer.send(any(ProducerRecord.class))).thenReturn(success, failure);
        var metadata = mock(RecordMetadata.class);
        when(metadata.partition()).thenReturn(1);
        @SuppressWarnings("unchecked")
        SendResult<String, ConnectorRecord> result = mock(SendResult.class);
        when(result.getRecordMetadata()).thenReturn(metadata);

        adapter.execute(event());
        success.complete(result);
        adapter.execute(event());
        failure.completeExceptionally(new IllegalStateException("down"));
    }

    @Test
    @DisplayName("rejects a producer that was not supplied")
    void shouldRejectAdapterWhenProducerIsMissing() {
        assertThatThrownBy(() -> new ProduceConnectorOutboundAdapter(null)).isInstanceOf(NullPointerException.class);
    }

    private static ConnectorEvent event() {
        return ConnectorEvent.from("1", "orders", "d", "postgres", List.of("a"), Operation.CREATED);
    }
}
