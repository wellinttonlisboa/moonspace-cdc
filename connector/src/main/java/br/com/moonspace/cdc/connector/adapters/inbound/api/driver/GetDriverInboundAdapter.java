package br.com.moonspace.cdc.connector.adapters.inbound.api.driver;

import br.com.moonspace.cdc.connector.infrastructure.api.driver.GetDriverAPI;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.get.GetDriverInboundQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class GetDriverInboundAdapter implements GetDriverAPI {

    private static final Logger LOG = LoggerFactory.getLogger(GetDriverInboundAdapter.class);

    private final GetDriverCoreApi getDriverCoreApi;

    public GetDriverInboundAdapter(GetDriverCoreApi getDriverCoreApi) {
        this.getDriverCoreApi = Objects.requireNonNull(getDriverCoreApi);
    }

    @Override
    public ResponseEntity<GetDriverInboundOutput> execute(String id) {
        LOG.atInfo()
                .setMessage("Starting search by driver")
                .log();
        return ResponseEntity.ok(this.getDriverCoreApi.execute(GetDriverInboundQuery.from(id)));
    }

}