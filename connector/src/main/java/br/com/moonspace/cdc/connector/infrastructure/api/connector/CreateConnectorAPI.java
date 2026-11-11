package br.com.moonspace.cdc.connector.infrastructure.api.connector;

import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.connector.create.CreateConnectorInboundOutput;
import br.com.moonspace.commons.rest.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/v1/connectors")
@Tag(name = "Connectors")
public interface CreateConnectorAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new connector")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created successfully",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = CreateConnectorInboundOutput.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "A validation error was thrown",
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
    ResponseEntity<CreateConnectorInboundOutput> execute(@RequestBody CreateConnectorInboundCommand command);

}