package br.com.moonspace.cdc.connector.adapters.inbound.api.driver;

import br.com.moonspace.cdc.connector.infrastructure.api.driver.PageableDriverAPI;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverCoreApi;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable.PageableDriverInboundQuery;
import br.com.moonspace.commons.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class PageableDriverInboundAdapter implements PageableDriverAPI {

    private static final Logger LOG = LoggerFactory.getLogger(PageableDriverInboundAdapter.class);

    private final PageableDriverCoreApi pageableDriverCoreApi;

    public PageableDriverInboundAdapter(PageableDriverCoreApi pageableDriverCoreApi) {
        this.pageableDriverCoreApi = Objects.requireNonNull(pageableDriverCoreApi);
    }

    @Override
    public ResponseEntity<Pagination<PageableDriverInboundOutput>> execute(String search,
                                                                           int page,
                                                                           int perPage,
                                                                           String sort,
                                                                           String direction) {
        LOG.atInfo()
                .setMessage("Starting paginated driver list returns")
                .log();
        return ResponseEntity.ok(this.pageableDriverCoreApi.execute(PageableDriverInboundQuery.from(page,
                perPage,
                search,
                sort,
                direction)));
    }

}