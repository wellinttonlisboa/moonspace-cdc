package br.com.moonspace.cdc.connector.adapters.inbound.api.driver;

import br.com.moonspace.cdc.connector.infrastructure.api.driver.DeleteDriverAPI;
import br.com.moonspace.cdc.connector.ports.inbound.driver.delete.DeleteDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.delete.DeleteDriverInboundCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class DeleteDriverInboundAdapter implements DeleteDriverAPI {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteDriverInboundAdapter.class);

    private final DeleteDriverCoreApi deleteDriverCoreApi;

    public DeleteDriverInboundAdapter(DeleteDriverCoreApi deleteDriverCoreApi) {
        this.deleteDriverCoreApi = Objects.requireNonNull(deleteDriverCoreApi);
    }

    @Override
    public void execute(String id) {
        LOG.atInfo()
                .setMessage("Starting driver deletion")
                .log();
        this.deleteDriverCoreApi.execute(DeleteDriverInboundCommand.from(id));
        LOG.atInfo()
                .setMessage("Driver deleted")
                .log();
    }

}