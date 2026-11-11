package br.com.moonspace.cdc.connector.adapters.inbound.api.connector;

import br.com.moonspace.cdc.connector.infrastructure.api.connector.CreateConnectorAPI;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CreateConnectorInboundAdapter implements CreateConnectorAPI {

    private static final Logger LOG = LoggerFactory.getLogger(CreateConnectorInboundAdapter.class);

    private final CreateConnectorCoreApi createConnectorCoreApi;

    public CreateConnectorInboundAdapter(CreateConnectorCoreApi createConnectorCoreApi) {
        this.createConnectorCoreApi = Objects.requireNonNull(createConnectorCoreApi);
    }

    @Override
    public ResponseEntity<CreateConnectorInboundOutput> execute(CreateConnectorInboundCommand command) {
        LOG.atInfo()
                .setMessage("Starting connector creation")
                .log();
        var output = this.createConnectorCoreApi.execute(command);
        return ResponseEntity
                .created(URI.create("/http/v1/connectors/" + output.id()))
                .body(output);
    }

}