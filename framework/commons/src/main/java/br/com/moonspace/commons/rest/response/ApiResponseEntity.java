package br.com.moonspace.commons.rest.response;

import br.com.moonspace.commons.rest.CommunicationExceptionHandler;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.InternalException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ApiResponseEntity {

    private ApiResponseEntity() {
    }

    /**
     * Method to make a response with body and https status OK.
     * @param t the ProtocolResponse.
     * @return the Response Entity with right object and status.
     */
    public static <T> ResponseEntity<T> success(T t) {
        return ResponseEntity.ok().body(t);
    }

    /**
     * Method to make a default Error Response with Exception response
     * @param code the right status code that you need to show in response.
     * @param e the Throwable error from exception.
     * @return the Response Entity with right object and status.
     */
    public static ResponseEntity<ApiError> exceptionFail(HttpStatusCode code, Throwable e) {
        try {
            return ResponseEntity.status(code).body(CommunicationExceptionHandler.handleException((DomainException) e));
        }catch (Exception exception) {
            return ResponseEntity.status(code).body(CommunicationExceptionHandler.handleException(InternalException.of("An error occurred, contact the Administrator!!")));
        }
    }

    /**
     * Method to make a response with no content and https status ACCEPTED.
     * @return the Response Entity with right status.
     */
    public static <T> ResponseEntity<T> noContent() {
        return ResponseEntity.noContent().build();
    }

    /**
     * Method to make a response with body and https status ACCEPTED.
     * @param t the ProtocolResponse.
     * @return the Response Entity with right object and status.
     */
    public static <T> ResponseEntity<T> accepted(T t) {
        return ResponseEntity.accepted().body(t);
    }

    /**
     * Method to make a default Error Response with Exception response
     * @param error the ExceptionResponse created by Communication Exception or manually.
     * @return the Response Entity with right object and status.
     */
    public static ResponseEntity<ApiError> fail(HttpStatusCode code, ApiError error) {
        return ResponseEntity.status(code).body(error);
    }

    /**
     * Method to make a default Error Response with Exception response
     * @param errors the ExceptionResponse created by Communication Exception or manually.
     * @return the Response Entity with right object and status.
     */
    public static ResponseEntity<ApiError> fail(HttpStatusCode code,
                                                String errorMessage,
                                                List<ApiValidationError> errors) {
        var error = new ApiError(errorMessage);
        errors.forEach(error::addSubError);
        return ResponseEntity.status(code).body(error);
    }

    /**
     * Method to make a default Error Response with Exception response
     * @param code the right status code that you need to show in response.
     * @param errorMessage the error message that you need to send.
     * @return the Response Entity with right object and status.
     */
    public static ResponseEntity<ApiError> fail(HttpStatusCode code,
                                                String errorMessage,
                                                Throwable e) {
        return ResponseEntity.status(code).body(new ApiError(errorMessage, e));
    }

}