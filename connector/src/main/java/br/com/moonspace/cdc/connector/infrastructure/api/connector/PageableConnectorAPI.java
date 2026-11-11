package br.com.moonspace.cdc.connector.infrastructure.api.connector;

import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable.PageableConnectorInboundOutput;
import br.com.moonspace.commons.pagination.Pagination;
import br.com.moonspace.commons.rest.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/v1/connectors/pageable")
@Tag(name = "Connectors")
public interface PageableConnectorAPI {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List pageable connectors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved successfully",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = Pagination.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An internal server error was thrown",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    }
            ),
    })
    ResponseEntity<Pagination<PageableConnectorInboundOutput>> execute(@RequestParam(name = "search", required = false, defaultValue = "") String search,
                                                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                                       @RequestParam(name = "perPage", required = false, defaultValue = "10") int perPage,
                                                                       @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
                                                                       @RequestParam(name = "dir", required = false, defaultValue = "asc") String direction);

}