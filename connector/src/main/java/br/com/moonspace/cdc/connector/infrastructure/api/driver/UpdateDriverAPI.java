package br.com.moonspace.cdc.connector.infrastructure.api.driver;

import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundCommand;
import br.com.moonspace.cdc.connector.ports.inbound.driver.update.UpdateDriverInboundOutput;
import br.com.moonspace.commons.rest.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/v1/drivers")
@Tag(name = "Drivers")
public interface UpdateDriverAPI {

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update an driver by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully",
                    content = {
                            @Content(
                                    schema = @Schema(implementation = UpdateDriverInboundOutput.class)
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
    ResponseEntity<UpdateDriverInboundOutput> execute(@RequestBody UpdateDriverInboundCommand command);

}