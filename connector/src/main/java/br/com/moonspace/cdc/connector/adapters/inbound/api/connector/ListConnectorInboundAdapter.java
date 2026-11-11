package br.com.moonspace.cdc.connector.adapters.inbound.api.connector;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.moonspace.cdc.connector.infrastructure.api.connector.ListConnectorAPI;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list.ListConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list.ListConnectorInboundOutput;

@RestController
public class ListConnectorInboundAdapter implements ListConnectorAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ListConnectorInboundAdapter.class);

    private final ListConnectorCoreApi listConnectorCoreApi;

    public ListConnectorInboundAdapter(ListConnectorCoreApi listConnectorCoreApi) {
        this.listConnectorCoreApi = Objects.requireNonNull(listConnectorCoreApi);
    }

    @Override
    public ResponseEntity<List<ListConnectorInboundOutput>> execute() {
        LOG.atInfo()
                .setMessage("Starting connector list returns")
                .log();
        return ResponseEntity.ok(this.listConnectorCoreApi.execute());
    }

}