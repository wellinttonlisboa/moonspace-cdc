package br.com.moonspace.cdc.connector.infrastructure.api.connector;

import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundOutput;
import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.list.ListConnectorInboundOutput;
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

import java.util.List;

@RequestMapping(value = "/v1/connectors")
@Tag(name = "Connectors")
public interface ListConnectorAPI {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "List all connectors")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved successfully",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = GetConnectorInboundOutput.class)
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
    ResponseEntity<List<ListConnectorInboundOutput>> execute();

}