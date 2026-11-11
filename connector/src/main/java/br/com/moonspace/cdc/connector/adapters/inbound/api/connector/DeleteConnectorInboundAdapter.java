package br.com.moonspace.cdc.connector.adapters.inbound.api.connector;

import br.com.moonspace.cdc.connector.infrastructure.api.connector.DeleteConnectorAPI;
import br.com.moonspace.cdc.connector.ports.inbound.connector.delete.DeleteConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.delete.DeleteConnectorInboundCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class DeleteConnectorInboundAdapter implements DeleteConnectorAPI {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteConnectorInboundAdapter.class);

    private final DeleteConnectorCoreApi deleteConnectorCoreApi;

    public DeleteConnectorInboundAdapter(DeleteConnectorCoreApi deleteConnectorCoreApi) {
        this.deleteConnectorCoreApi = Objects.requireNonNull(deleteConnectorCoreApi);
    }

    @Override
    public void execute(String id) {
        LOG.atInfo()
                .setMessage("Starting connector deletion")
                .log();
        this.deleteConnectorCoreApi.execute(DeleteConnectorInboundCommand.from(id));
    }

}