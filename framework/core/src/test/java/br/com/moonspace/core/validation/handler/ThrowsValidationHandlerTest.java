package br.com.moonspace.core.validation.handler;

import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.support.UnitTest;
import br.com.moonspace.core.validation.Error;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class ThrowsValidationHandlerTest {

    private final ThrowsValidationHandler handler = new ThrowsValidationHandler();

    @Test
    @DisplayName("throws as soon as an error is appended")
    void shouldThrowWhenErrorIsAppended() {
        assertThatThrownBy(() -> handler.append(Error.of("invalid")))
                .isInstanceOf(DomainException.class)
                .hasMessage("invalid");
    }

    @Test
    @DisplayName("throws the errors collected by another handler")
    void shouldThrowWhenAnotherHandlerIsAppended() {
        var notification = Notification.create(Error.of("from notification"));

        assertThatThrownBy(() -> handler.append(notification))
                .isInstanceOf(DomainException.class)
                .satisfies(ex -> assertThat(((DomainException) ex).getErrors())
                        .containsExactly(Error.of("from notification")));
    }

    @Test
    @DisplayName("returns the value when validation passes")
    void shouldReturnValueWhenValidationSucceeds() {
        assertThat(handler.validate(() -> 7)).isEqualTo(7);
        assertThat(handler.getErrors()).isEmpty();
        assertThat(handler.hasError()).isFalse();
        assertThat(handler.firstError()).isNull();
    }

    @Test
    @DisplayName("wraps a failed validation as a domain exception")
    void shouldThrowDomainExceptionWhenValidationFails() {
        assertThatThrownBy(() -> handler.validate(() -> {
            throw new IllegalStateException("failed");
        }))
                .isInstanceOf(DomainException.class)
                .hasMessage("failed");
    }
}
