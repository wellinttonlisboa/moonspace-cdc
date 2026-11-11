package br.com.moonspace.cdc.connector.adapters.inbound.api.driver;

import br.com.moonspace.cdc.connector.infrastructure.api.driver.UpdateDriverAPI;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class UpdateDriverInboundAdapter implements UpdateDriverAPI {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateDriverInboundAdapter.class);

    private final UpdateDriverCoreApi updateDriverCoreApi;

    public UpdateDriverInboundAdapter(UpdateDriverCoreApi updateDriverCoreApi) {
        this.updateDriverCoreApi = Objects.requireNonNull(updateDriverCoreApi);
    }

    @Override
    public ResponseEntity<UpdateDriverInboundOutput> execute(UpdateDriverInboundCommand command) {
        LOG.atInfo()
                .setMessage("Starting driver update")
                .log();
        var output = this.updateDriverCoreApi.execute(command);
        LOG.atInfo()
                .setMessage("Driver updated")
                .log();
        return ResponseEntity
                .created(URI.create("/http/v1/drivers/" + output.id()))
                .body(output);
    }

}