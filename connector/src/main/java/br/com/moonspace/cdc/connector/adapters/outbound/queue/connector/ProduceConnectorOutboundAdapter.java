package br.com.moonspace.cdc.connector.adapters.outbound.queue.connector;

import br.com.moonspace.cdc.connector.core.domain.events.connector.ConnectorEvent;
import br.com.moonspace.cdc.connector.ports.outbound.queue.ProduceConnectorOutboundPort;
import br.com.moonspace.schema.registry.v1.connector.ConnectorRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static java.util.Objects.isNull;

@Component
public class ProduceConnectorOutboundAdapter implements ProduceConnectorOutboundPort {

    @Value("${kafka.producer.connector.topic}")
    private String topic;

    private static final Logger LOG = LoggerFactory.getLogger(ProduceConnectorOutboundAdapter.class);

    private final KafkaTemplate<String, ConnectorRecord> producer;

    public ProduceConnectorOutboundAdapter(KafkaTemplate<String, ConnectorRecord> producer) {
        this.producer = Objects.requireNonNull(producer);
    }

    @Override
    public void execute(ConnectorEvent event) {
        LOG.atInfo()
                .setMessage("Sending event '{}'")
                .addArgument(event.getClass().getSimpleName())
                .log();
        var payload = new ConnectorRecord(event.id(), event.name(), event.driverId(), event.driverId(),
                event.properties(), event.operation(), event.occurredOn());
        var key = ConnectorRecord.class.getSimpleName();
        var producerRecord = new ProducerRecord<>(topic, key, payload);
        var response = this.producer.send(producerRecord);
        response.whenComplete((result, error) -> {
            if(isNull(result)) {
                LOG.atInfo()
                        .setMessage("Error to send message, the exception is '{}'")
                        .addArgument(error.getMessage())
                        .log();
            } else {
                LOG.atInfo()
                        .setMessage("Message Sent SuccessFully for the key: '{}' and the value is '{}' | partition is '{}'")
                        .addArgument(key)
                        .addArgument(payload)
                        .addArgument(result.getRecordMetadata().partition())
                        .log();
            }
        });
    }

}