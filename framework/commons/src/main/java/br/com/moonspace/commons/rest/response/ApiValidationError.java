package br.com.moonspace.commons.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiValidationError extends ApiSubError {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    /**
     * Constructor with field and message error.
     *
     * @param message Custom message on annotation.
     */
    ApiValidationError(String message) {
        this.message = message;
    }

    /**
     * Constructor with field and message error.
     *
     * @param object Field error.
     * @param message Custom message on annotation.
     */
    ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public static ApiValidationError of(String message) {
        return new ApiValidationError(message);
    }

}