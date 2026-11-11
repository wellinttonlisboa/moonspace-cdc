package br.com.moonspace.cdc.connector.adapters.inbound.api.connector;

import br.com.moonspace.cdc.connector.infrastructure.api.connector.GetConnectorAPI;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class GetConnectorInboundAdapter implements GetConnectorAPI {

    private static final Logger LOG = LoggerFactory.getLogger(GetConnectorInboundAdapter.class);

    private final GetConnectorCoreApi getConnectorCoreApi;

    public GetConnectorInboundAdapter(GetConnectorCoreApi getConnectorCoreApi) {
        this.getConnectorCoreApi = Objects.requireNonNull(getConnectorCoreApi);
    }

    @Override
    public ResponseEntity<GetConnectorInboundOutput> execute(String id) {
        LOG.atInfo()
                .setMessage("Starting search by connector")
                .log();
        return ResponseEntity.ok(this.getConnectorCoreApi.execute(GetConnectorInboundQuery.from(id)));
    }

}