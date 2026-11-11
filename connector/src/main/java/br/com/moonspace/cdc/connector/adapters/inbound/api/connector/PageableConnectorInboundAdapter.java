package br.com.moonspace.cdc.connector.adapters.inbound.api.connector;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.moonspace.cdc.connector.infrastructure.api.connector.PageableConnectorAPI;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundQuery;
import br.com.moonspace.commons.pagination.Pagination;

@RestController
public class PageableConnectorInboundAdapter implements PageableConnectorAPI {

    private static final Logger LOG = LoggerFactory.getLogger(PageableConnectorInboundAdapter.class);

    private final PageableConnectorCoreApi pageableConnectorCoreApi;

    public PageableConnectorInboundAdapter(PageableConnectorCoreApi pageableConnectorCoreApi) {
        this.pageableConnectorCoreApi = Objects.requireNonNull(pageableConnectorCoreApi);
    }

    @Override
    public ResponseEntity<Pagination<PageableConnectorInboundOutput>> execute(String search,
                                                                              int page,
                                                                              int perPage,
                                                                              String sort,
                                                                              String direction) {
        LOG.atInfo()
                .setMessage("Starting paginated connector list returns")
                .log();
        return ResponseEntity.ok(this.pageableConnectorCoreApi.execute(PageableConnectorInboundQuery.from(page,
                perPage,
                search,
                sort,
                direction)));
    }

}