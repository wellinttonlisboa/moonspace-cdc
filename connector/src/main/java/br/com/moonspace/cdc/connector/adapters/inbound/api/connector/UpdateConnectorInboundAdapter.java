package br.com.moonspace.cdc.connector.adapters.inbound.api.connector;

import br.com.moonspace.cdc.connector.infrastructure.api.connector.UpdateConnectorAPI;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.update.UpdateConnectorInboundOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class UpdateConnectorInboundAdapter implements UpdateConnectorAPI {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateConnectorInboundAdapter.class);

    private final UpdateConnectorCoreApi updateConnectorCoreApi;

    public UpdateConnectorInboundAdapter(UpdateConnectorCoreApi updateConnectorCoreApi) {
        this.updateConnectorCoreApi = Objects.requireNonNull(updateConnectorCoreApi);
    }

    @Override
    public ResponseEntity<UpdateConnectorInboundOutput> execute(UpdateConnectorInboundCommand command) {
        LOG.atInfo()
                .setMessage("Starting connector update")
                .log();
        var output = this.updateConnectorCoreApi.execute(command);
        return ResponseEntity
                .created(URI.create("/http/v1/connectors/" + output.id()))
                .body(output);
    }

}