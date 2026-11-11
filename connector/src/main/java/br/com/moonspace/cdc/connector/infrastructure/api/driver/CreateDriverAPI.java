package br.com.moonspace.cdc.connector.infrastructure.api.driver;

import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.create.CreateDriverInboundOutput;
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

@RequestMapping(value = "/v1/drivers")
@Tag(name = "Drivers")
public interface CreateDriverAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new driver")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created successfully",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = CreateDriverInboundOutput.class)
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
    ResponseEntity<CreateDriverInboundOutput> execute(@RequestBody CreateDriverInboundCommand command);

}