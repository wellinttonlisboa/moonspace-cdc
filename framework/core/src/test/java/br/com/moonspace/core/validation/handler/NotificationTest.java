package br.com.moonspace.core.validation.handler;

import br.com.moonspace.core.architecture.basic.ddd.exceptions.DomainException;
import br.com.moonspace.core.support.UnitTest;
import br.com.moonspace.core.validation.Error;
import br.com.moonspace.core.validation.ValidationHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class NotificationTest {

    @Test
    @DisplayName("starts without errors")
    void shouldStartEmptyWhenCreated() {
        var notification = Notification.create();

        assertThat(notification.hasError()).isFalse();
        assertThat(notification.firstError()).isNull();
        assertThat(notification.getErrors()).isEmpty();
        assertThat(notification.toString()).isEmpty();
    }

    @Test
    @DisplayName("collects the message of a thrown cause")
    void shouldCaptureCauseMessageWhenCreatedFromThrowable() {
        var notification = Notification.create(new IllegalStateException("broken"));

        assertThat(notification.firstError()).isEqualTo(Error.of("broken"));
        assertThat(notification.hasError()).isTrue();
    }

    @Test
    @DisplayName("appends errors from another handler")
    void shouldAppendErrorsWhenAnotherHandlerIsAdded() {
        var notification = Notification.create(Error.of("first"))
                .append(Error.of("second"))
                .append(Notification.create(Error.of("third")));

        assertThat(notification.getErrors())
                .extracting(Error::error)
                .containsExactly("first", "second", "third");
        assertThat(notification.toString()).isEqualTo("first, second, third, ");
        assertThat(notification.firstError()).isEqualTo(Error.of("first"));
    }

    @Test
    @DisplayName("returns the validated value when the rule passes")
    void shouldReturnValueWhenValidationSucceeds() {
        var notification = Notification.create();

        var value = notification.validate(() -> "ok");

        assertThat(value).isEqualTo("ok");
        assertThat(notification.hasError()).isFalse();
    }

    @Test
    @DisplayName("collects domain errors and returns null")
    void shouldCollectDomainErrorsWhenValidationFails() {
        var notification = Notification.create();

        var value = notification.validate(() -> {
            throw DomainException.of(List.of(Error.of("a"), Error.of("b")));
        });

        assertThat(value).isNull();
        assertThat(notification.getErrors()).extracting(Error::error).containsExactly("a", "b");
    }

    @Test
    @DisplayName("collects an unexpected failure as a single error")
    void shouldCollectUnexpectedErrorWhenValidationThrows() {
        var notification = Notification.create();

        var value = notification.validate(() -> {
            throw new IllegalArgumentException("nope");
        });

        assertThat(value).isNull();
        assertThat(notification.firstError()).isEqualTo(Error.of("nope"));
    }

    @Test
    @DisplayName("treats a missing error list as no error")
    void shouldTreatMissingErrorsAsEmptyWhenHandlerReturnsNull() {
        ValidationHandler handler = new NullErrorsHandler();

        assertThat(handler.hasError()).isFalse();
        assertThat(handler.firstError()).isNull();
    }

    private static final class NullErrorsHandler implements ValidationHandler {

        @Override
        public ValidationHandler append(Error error) {
            return this;
        }

        @Override
        public ValidationHandler append(ValidationHandler handler) {
            return this;
        }

        @Override
        public <T> T validate(Validation<T> validation) {
            return validation.validate();
        }

        @Override
        public List<Error> getErrors() {
            return null;
        }
    }
}
