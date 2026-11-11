package br.com.moonspace.cdc.connector.infrastructure.api.connector;

import br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.get.GetConnectorInboundOutput;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/v1/connectors")
@Tag(name = "Connectors")
public interface GetConnectorAPI {

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a connector by it's identifier")
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
                    responseCode = "404",
                    description = "Entity was not found",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = ApiError.class)
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
    ResponseEntity<GetConnectorInboundOutput> execute(@PathVariable String id);

}