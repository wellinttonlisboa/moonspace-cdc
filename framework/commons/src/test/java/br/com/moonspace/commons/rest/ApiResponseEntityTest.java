package br.com.moonspace.commons.rest;

import br.com.moonspace.commons.rest.response.ApiError;
import br.com.moonspace.commons.rest.response.ApiResponseEntity;
import br.com.moonspace.commons.rest.response.ApiValidationError;
import br.com.moonspace.commons.support.UnitTest;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.validation.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class ApiResponseEntityTest {

    @Test
    @DisplayName("returns the body with OK, accepted or no content")
    void shouldReturnBodyWhenCallSucceeds() throws Exception {
        assertThat(ApiResponseEntity.success("ok")).extracting(response -> response.getBody()).isEqualTo("ok");
        assertThat(ApiResponseEntity.accepted("queued").getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(ApiResponseEntity.noContent().getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        var constructor = ApiResponseEntity.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance()).isNotNull();
    }

    @Test
    @DisplayName("returns the supplied error body")
    void shouldReturnErrorBodyWhenFailureIsAlreadyBuilt() {
        var error = new ApiError("invalid");

        var response = ApiResponseEntity.fail(HttpStatus.BAD_REQUEST, error);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isSameAs(error);
    }

    @Test
    @DisplayName("attaches validation errors to the failure")
    void shouldAttachValidationErrorsWhenFailureListsThem() {
        var response = ApiResponseEntity.fail(HttpStatus.BAD_REQUEST, "invalid", List.of(ApiValidationError.of("name")));

        assertThat(response.getBody().getMessage()).isEqualTo("invalid");
        assertThat(response.getBody().getSubErrors()).hasSize(1);
    }

    @Test
    @DisplayName("keeps the cause text on a failure")
    void shouldKeepCauseWhenFailureHasThrowable() {
        var response = ApiResponseEntity.fail(HttpStatus.CONFLICT, "conflict", new IllegalStateException("dup"));

        assertThat(response.getBody().getDebugMessage()).isEqualTo("dup");
    }

    @Test
    @DisplayName("maps a domain exception through the communication handler")
    void shouldMapDomainExceptionWhenFailureIsDomain() throws Exception {
        var handler = CommunicationExceptionHandler.class.getDeclaredConstructor();
        handler.setAccessible(true);
        assertThat(handler.newInstance()).isNotNull();

        var response = ApiResponseEntity.exceptionFail(HttpStatus.BAD_REQUEST, DomainException.of(Error.of("name")));

        assertThat(response.getBody().getMessage()).isEqualTo("name");
        assertThat(response.getBody().getSubErrors()).hasSize(1);
    }

    @Test
    @DisplayName("hides an unexpected failure behind an internal message")
    void shouldHideUnexpectedFailureWhenThrowableIsNotDomain() {
        var response = ApiResponseEntity.exceptionFail(HttpStatus.INTERNAL_SERVER_ERROR, new IllegalStateException("leak"));

        assertThat(response.getBody().getMessage()).isEqualTo("An error occurred, contact the Administrator!!");
    }
}
