package br.com.moonspace.cdc.connector.infrastructure.config.exceptionhandler;

import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.google.common.collect.Lists;

import br.com.moonspace.commons.rest.response.ApiError;
import br.com.moonspace.commons.rest.response.ApiResponseEntity;
import br.com.moonspace.commons.rest.response.ApiValidationError;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.architecture.basic.ddd.exceptions.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;

/**
 * Maps the exceptions into an HTTP status.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        return ApiResponseEntity.exceptionFail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ApiError> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        StringBuilder strings = new StringBuilder();
        strings.append(e.getContentType());
        strings.append(" media type is not supported. Supported media types are ");
        e.getSupportedMediaTypes().forEach(media -> strings.append(media).append(", "));
        return ApiResponseEntity.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, strings.substring(0, strings.length() - 2), e);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ApiError> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        String error = e.getParameterName() + " parameter is missing";
        return ApiResponseEntity.fail(HttpStatus.BAD_REQUEST, error, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        ApiError apiError = new ApiError("Validation error");
        apiError.addValidationErrors(e.getBindingResult().getFieldErrors());
        apiError.addValidationError(e.getBindingResult().getGlobalErrors());
        return ApiResponseEntity.fail(HttpStatus.BAD_REQUEST, apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        String error = "Malformed JSON request";
        return ApiResponseEntity.fail(HttpStatus.BAD_REQUEST, error, e);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    protected ResponseEntity<ApiError> handleHttpMessageNotWritable(HttpMessageNotWritableException e) {
        String error = "Error writing JSON output";
        return ApiResponseEntity.fail(HttpStatus.INTERNAL_SERVER_ERROR, error, e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ApiResponseEntity.fail(HttpStatus.NOT_FOUND, "Could not find the %s method for URL %s".formatted(e.getHttpMethod(),
                e.getRequestURL()), e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        if (e.getCause() instanceof ConstraintViolationException) {
            return ApiResponseEntity.fail(HttpStatus.CONFLICT, "Database error", e.getCause());
        }
        return ApiResponseEntity.fail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", e.getCause());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ApiResponseEntity.fail(HttpStatus.BAD_REQUEST, "The parameter '%s' of value '%s' could not be converted to type '%s'".formatted(e.getName(),
                e.getValue(), Objects.requireNonNull(e.getRequiredType()).getSimpleName()), e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ApiResponseEntity.fail(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), e);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException e) {
        return ApiResponseEntity.fail(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
    }

    @ExceptionHandler(DomainException.class)
    protected ResponseEntity<ApiError> handleDomainException(DomainException e) {
        var errors = CollectionUtils.isEmpty(e.getErrors()) ? Lists.<ApiValidationError>newArrayList() : e.getErrors()
                .stream()
                .map(error -> ApiValidationError.of(error.error()))
                .toList();
        return ApiResponseEntity.fail(HttpStatus.BAD_REQUEST, e.getMessage(), errors);
    }

}