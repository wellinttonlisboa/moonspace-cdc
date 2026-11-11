package br.com.moonspace.cdc.router.adapters.inbound.queue.connector;

import br.com.moonspace.cdc.router.infrastructure.queue.connector.ConnectorStream;
import br.com.moonspace.cdc.router.ports.inbound.connector.create.CreateConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.router.ports.inbound.connector.delete.DeleteConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.delete.DeleteConnectorInboundCommand;
import br.com.moonspace.cdc.router.ports.inbound.connector.update.UpdateConnectorCoreApi;
import br.com.moonspace.cdc.router.ports.inbound.connector.update.UpdateConnectorInboundCommand;
import br.com.moonspace.schema.registry.v1.connector.ConnectorRecord;
import br.com.moonspace.schema.registry.v1.connector.Operation;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ConnectorInboundAdapter implements ConnectorStream {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectorInboundAdapter.class);

    private final CreateConnectorCoreApi createConnectorCoreApi;
    private final UpdateConnectorCoreApi updateConnectorCoreApi;
    private final DeleteConnectorCoreApi deleteConnectorCoreApi;

    public ConnectorInboundAdapter(CreateConnectorCoreApi createConnectorCoreApi,
                                   UpdateConnectorCoreApi updateConnectorCoreApi,
                                   DeleteConnectorCoreApi deleteConnectorCoreApi) {
        this.createConnectorCoreApi = Objects.requireNonNull(createConnectorCoreApi);
        this.updateConnectorCoreApi = Objects.requireNonNull(updateConnectorCoreApi);
        this.deleteConnectorCoreApi = Objects.requireNonNull(deleteConnectorCoreApi);
    }

    @Override
    @KafkaListener(topics = "${kafka.consumer.connector.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void on(ConsumerRecord<String, ConnectorRecord> consumerRecord) {
        LOG.atInfo()
                .setMessage("Receiving event")
                .log();
        var payload = consumerRecord.value();
        var properties = Lists.newArrayList(payload.getProperties());
        switch (payload.getOperation()) {
            case Operation.CREATED -> {
                var command = CreateConnectorInboundCommand.from(payload.getId(), payload.getName(),
                        payload.getDriverId(), payload.getDriverName(), properties);
                this.createConnectorCoreApi.execute(command);
            }
            case Operation.UPDATED -> {
                var command = UpdateConnectorInboundCommand.from(payload.getId(), payload.getName(),
                        payload.getDriverId(), payload.getDriverName(), properties);
                this.updateConnectorCoreApi.execute(command);
            }
            default -> {
                var command = DeleteConnectorInboundCommand.from(payload.getId());
                this.deleteConnectorCoreApi.execute(command);
            }
        }
    }

}