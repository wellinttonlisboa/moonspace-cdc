package br.com.moonspace.cdc.connector.adapters.inbound.api.driver;

import br.com.moonspace.cdc.connector.infrastructure.api.driver.ListDriverAPI;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list.ListDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.list.ListDriverInboundOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ListDriverInboundAdapter implements ListDriverAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ListDriverInboundAdapter.class);

    private final ListDriverCoreApi listDriverCoreApi;

    public ListDriverInboundAdapter(ListDriverCoreApi listDriverCoreApi) {
        this.listDriverCoreApi = Objects.requireNonNull(listDriverCoreApi);
    }

    @Override
    public ResponseEntity<List<ListDriverInboundOutput>> execute() {
        LOG.atInfo()
                .setMessage("Starting driver list returns")
                .log();
        return ResponseEntity.ok(this.listDriverCoreApi.execute());
    }

}