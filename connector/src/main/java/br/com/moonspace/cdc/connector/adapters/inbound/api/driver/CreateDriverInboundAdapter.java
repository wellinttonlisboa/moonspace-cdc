package br.com.moonspace.cdc.connector.adapters.inbound.api.driver;

import br.com.moonspace.cdc.connector.infrastructure.api.driver.CreateDriverAPI;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CreateDriverInboundAdapter implements CreateDriverAPI {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDriverInboundAdapter.class);

    private final CreateDriverCoreApi createDriverCoreApi;

    public CreateDriverInboundAdapter(CreateDriverCoreApi createDriverCoreApi) {
        this.createDriverCoreApi = Objects.requireNonNull(createDriverCoreApi);
    }

    @Override
    public ResponseEntity<CreateDriverInboundOutput> execute(CreateDriverInboundCommand command) {
        LOG.atInfo()
                .setMessage("Starting driver creation")
                .log();
        var output = this.createDriverCoreApi.execute(command);
        LOG.atInfo()
                .setMessage("Driver created")
                .log();
        return ResponseEntity
                .created(URI.create("/http/v1/drivers/" + output.id()))
                .body(output);
    }

}