package br.com.moonspace.cdc.connector.infrastructure.config.exceptionhandler;

import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.UnauthorizedException;
import br.com.moonspace.core.validation.Error;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@org.junit.jupiter.api.Tag("unit")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("maps an unexpected failure to 500")
    void shouldReturnInternalErrorWhenFailureIsUnexpected() {
        var response = handler.handleException(new IllegalStateException("boom"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).isEqualTo("An error occurred, contact the Administrator!!");
    }

    @Test
    @DisplayName("lists the media types the endpoint accepts")
    void shouldListSupportedMediaWhenTypeIsRejected() {
        var response = handler.handleHttpMediaTypeNotSupported(new HttpMediaTypeNotSupportedException(
                MediaType.TEXT_PLAIN, List.of(MediaType.APPLICATION_JSON)));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody().getMessage()).contains("text/plain").contains("application/json");
    }

    @Test
    @DisplayName("names the parameter that is missing")
    void shouldNameParameterWhenItIsMissing() {
        var response = handler.handleMissingServletRequestParameter(
                new MissingServletRequestParameterException("id", "String"));

        assertThat(response.getBody().getMessage()).isEqualTo("id parameter is missing");
    }

    @Test
    @DisplayName("collects field and global validation errors")
    void shouldCollectBindingErrorsWhenArgumentIsInvalid() throws Exception {
        var binding = new BeanPropertyBindingResult(new Object(), "connector");
        binding.addError(new FieldError("connector", "name", "", false, null, null, "blank"));
        binding.addError(new ObjectError("connector", "invalid"));
        Method method = String.class.getMethod("valueOf", Object.class);

        var response = handler.handleMethodArgumentNotValid(new MethodArgumentNotValidException(
                new org.springframework.core.MethodParameter(method, 0), binding));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getSubErrors()).hasSize(2);
    }

    @Test
    @DisplayName("rejects a body that is not readable")
    void shouldRejectBodyWhenJsonIsMalformed() {
        var response = handler.handleHttpMessageNotReadable(
                new HttpMessageNotReadableException("bad", new IllegalArgumentException("bad"), null));

        assertThat(response.getBody().getMessage()).isEqualTo("Malformed JSON request");
    }

    @Test
    @DisplayName("reports a failure while writing the response")
    void shouldReportWriteFailureWhenJsonCannotBeWritten() {
        var response = handler.handleHttpMessageNotWritable(new HttpMessageNotWritableException("write"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).isEqualTo("Error writing JSON output");
    }

    @Test
    @DisplayName("describes the route that was not found")
    void shouldDescribeRouteWhenHandlerIsMissing() {
        var response = handler.handleNoHandlerFoundException(
                new NoHandlerFoundException("GET", "/missing", new HttpHeaders()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).contains("GET").contains("/missing");
    }

    @Test
    @DisplayName("returns a conflict when the database rejects a constraint")
    void shouldReturnConflictWhenConstraintIsViolated() {
        var response = handler.handleDataIntegrityViolation(new DataIntegrityViolationException(
                "dup", new ConstraintViolationException("dup", Set.of())));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("Database error");
    }

    @Test
    @DisplayName("returns 500 when the database failure is not a constraint")
    void shouldReturnInternalErrorWhenDatabaseFailureIsUnexpected() {
        var response = handler.handleDataIntegrityViolation(new DataIntegrityViolationException(
                "down", new IllegalStateException("down")));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getMessage()).isEqualTo("Unexpected error");
    }

    @Test
    @DisplayName("describes a parameter that cannot be converted")
    void shouldDescribeParameterWhenTypeDoesNotMatch() throws Exception {
        Method method = String.class.getMethod("valueOf", Object.class);
        var response = handler.handleMethodArgumentTypeMismatch(new MethodArgumentTypeMismatchException(
                "abc", Integer.class, "page", new org.springframework.core.MethodParameter(method, 0),
                new IllegalArgumentException("abc")));

        assertThat(response.getBody().getMessage()).contains("page").contains("abc").contains("Integer");
    }

    @Test
    @DisplayName("fails when the expected type is missing")
    void shouldFailWhenRequiredTypeIsMissing() throws Exception {
        Method method = String.class.getMethod("valueOf", Object.class);

        assertThatThrownBy(() -> handler.handleMethodArgumentTypeMismatch(new MethodArgumentTypeMismatchException(
                "abc", null, "page", new org.springframework.core.MethodParameter(method, 0), null)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("rejects an HTTP method the route does not allow")
    void shouldRejectMethodWhenItIsNotAllowed() {
        var response = handler.handleHttpRequestMethodNotSupportedException(
                new HttpRequestMethodNotSupportedException("PATCH"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    @DisplayName("rejects a caller that is not authorized")
    void shouldRejectCallerWhenUnauthorized() {
        var response = handler.handleUnauthorizedException(UnauthorizedException.of("denied"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("returns domain errors as a bad request")
    void shouldReturnDomainErrorsWhenRuleFails() {
        var response = handler.handleDomainException(DomainException.of(Error.of("name")));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getSubErrors()).hasSize(1);
    }

    @Test
    @DisplayName("returns an empty error list when the domain exception has none")
    void shouldReturnEmptyErrorsWhenDomainExceptionHasNone() {
        var response = handler.handleDomainException(DomainException.of(List.of()));

        assertThat(response.getBody().getSubErrors()).isNull();
    }
}
