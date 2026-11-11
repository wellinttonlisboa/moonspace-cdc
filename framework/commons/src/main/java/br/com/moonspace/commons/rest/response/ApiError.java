package br.com.moonspace.commons.rest.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import br.com.moonspace.core.validation.Error;
import jakarta.validation.ConstraintViolation;
import lombok.Data;

@Data
@JsonIgnoreProperties(value="errors")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(Throwable e) {
        this();
        this.message = "Unexpected error";
        this.debugMessage = e.getLocalizedMessage();
    }

    public ApiError(String message) {
        this();
        this.message = message;
    }

    public ApiError(String message, Throwable e) {
        this();
        this.message = message;
        this.debugMessage = e.getLocalizedMessage();
    }

    public void addSubError(ApiSubError error) {
        if (subErrors == null) {
            subErrors = Lists.newArrayList();
        }
        subErrors.add(error);
    }

    public void addSubError(List<ApiSubError> errors) {
        if (subErrors == null) {
            subErrors = Lists.newArrayList();
        }
        subErrors.addAll(errors);
    }

    public List<Error> getErrors() {
        return CollectionUtils.isEmpty(this.subErrors) ? null : this.subErrors
                .stream()
                .map(ApiValidationError.class::cast)
                .map(error -> Error.of(error.getMessage()))
                .toList();
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    private void addValidationError(FieldError error) {
        this.addValidationError(
                error.getObjectName(),
                error.getField(),
                error.getRejectedValue(),
                error.getDefaultMessage());
    }

    private void addValidationError(ObjectError error) {
        this.addValidationError(error.getObjectName(), error.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> errors) {
        errors.forEach(this::addValidationError);
    }

    public void addValidationErrors(List<FieldError> errors) {
        errors.forEach(this::addValidationError);
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraints) {
        constraints.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
     *
     * @param constraint the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> constraint) {
        this.addValidationError(
                constraint.getRootBeanClass().getSimpleName(),
                ((PathImpl) constraint.getPropertyPath()).getLeafNode().asString(),
                constraint.getInvalidValue(),
                constraint.getMessage());
    }

}