package br.com.moonspace.cdc.router.infrastructure.queue.connector;

import br.com.moonspace.schema.registry.v1.connector.ConnectorRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.messaging.handler.annotation.Payload;

public interface ConnectorStream {

    void on(@Payload ConsumerRecord<String, ConnectorRecord> consumerRecord);

}