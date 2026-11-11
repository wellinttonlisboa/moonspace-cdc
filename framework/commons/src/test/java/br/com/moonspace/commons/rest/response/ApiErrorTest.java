package br.com.moonspace.commons.rest.response;

import br.com.moonspace.commons.support.UnitTest;
import br.com.moonspace.core.validation.Error;
import jakarta.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class ApiErrorTest {

    @Test
    @DisplayName("records an unexpected failure")
    void shouldRecordUnexpectedFailureWhenCreatedFromThrowable() {
        var error = new ApiError(new IllegalStateException("boom"));

        assertThat(error.getMessage()).isEqualTo("Unexpected error");
        assertThat(error.getDebugMessage()).isEqualTo("boom");
        assertThat(error.getTimestamp()).isNotNull();
        assertThat(error.getErrors()).isNull();
    }

    @Test
    @DisplayName("keeps a message without a cause")
    void shouldKeepMessageWhenCreatedFromText() {
        var error = new ApiError("invalid");

        assertThat(error.getMessage()).isEqualTo("invalid");
        assertThat(error.getDebugMessage()).isNull();
    }

    @Test
    @DisplayName("keeps both the message and the cause")
    void shouldKeepCauseWhenCreatedFromMessageAndThrowable() {
        var error = new ApiError("invalid", new IllegalArgumentException("why"));

        assertThat(error.getMessage()).isEqualTo("invalid");
        assertThat(error.getDebugMessage()).isEqualTo("why");
    }

    @Test
    @DisplayName("accumulates sub-errors")
    void shouldAccumulateSubErrorsWhenAddedMoreThanOnce() {
        var error = new ApiError("invalid");
        var first = ApiValidationError.of("first");
        var second = ApiValidationError.of("second");

        error.addSubError(first);
        error.addSubError(List.of(second));

        assertThat(error.getSubErrors()).containsExactly(first, second);
        assertThat(error.getErrors()).containsExactly(Error.of("first"), Error.of("second"));
    }

    @Test
    @DisplayName("turns field, object and constraint violations into sub-errors")
    void shouldAddValidationErrorsWhenBindingFails() {
        var error = new ApiError("Validation error");
        var field = new FieldError("connector", "name", "x", false, null, null, "too short");
        var global = new ObjectError("connector", "invalid");
        ConstraintViolation<?> violation = violation();

        error.addValidationErrors(List.of(field));
        error.addValidationError(List.of(global));
        error.addValidationErrors(Set.of(violation));

        assertThat(error.getSubErrors()).hasSize(3);
        assertThat(error.getErrors()).extracting(Error::error)
                .containsExactly("too short", "invalid", "must not be blank");
    }

    @SuppressWarnings("unchecked")
    private static ConstraintViolation<?> violation() {
        ConstraintViolation<String> violation = mock(ConstraintViolation.class);
        when(violation.getRootBeanClass()).thenReturn(String.class);
        when(violation.getPropertyPath()).thenReturn(PathImpl.createPathFromString("name"));
        when(violation.getInvalidValue()).thenReturn("");
        when(violation.getMessage()).thenReturn("must not be blank");
        return violation;
    }
}
