package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.support.Samples.SampleAggregate;
import br.com.moonspace.core.support.Samples.SampleId;
import br.com.moonspace.core.support.UnitTest;
import br.com.moonspace.core.validation.Error;
import br.com.moonspace.core.validation.handler.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class DomainExceptionTest {

    @Test
    @DisplayName("carries a single error as its message")
    void shouldExposeSingleErrorWhenCreatedFromOneError() {
        var exception = DomainException.of(Error.of("invalid"));

        assertThat(exception).hasMessage("invalid");
        assertThat(exception.getErrors()).containsExactly(Error.of("invalid"));
        assertThat(exception.getStackTrace()).isEmpty();
    }

    @Test
    @DisplayName("keeps every error when created from a list")
    void shouldExposeEveryErrorWhenCreatedFromAList() {
        var exception = DomainException.of(List.of(Error.of("a"), Error.of("b")));

        assertThat(exception).hasMessage("");
        assertThat(exception.getErrors()).extracting(Error::error).containsExactly("a", "b");
    }

    @Test
    @DisplayName("preserves the cause without a stack trace")
    void shouldKeepCauseWhenCreatedWithOne() {
        var cause = new IllegalStateException("root");

        var exception = new NoStacktraceException("wrapped", cause);

        assertThat(exception).hasMessage("wrapped").hasCause(cause);
        assertThat(exception.getStackTrace()).isEmpty();
    }

    @Nested
    @DisplayName("specialized domain exceptions")
    class Specialized {

        @Test
        @DisplayName("describes a missing aggregate")
        void shouldDescribeMissingAggregateWhenNotFound() {
            var exception = NotFoundException.of(SampleAggregate.class, new SampleId("42"));

            assertThat(exception).hasMessage("SampleAggregate with ID 42 was not found");
            assertThat(exception.getErrors()).isEmpty();
        }

        @Test
        @DisplayName("describes a duplicate name")
        void shouldDescribeDuplicateWhenNameAlreadyExists() {
            var exception = AlreadyExistsException.of("Driver", "postgres");

            assertThat(exception).hasMessage("Driver with Name postgres already exists");
        }

        @Test
        @DisplayName("carries the caller message for a bad request")
        void shouldCarryMessageWhenRequestIsBad() {
            assertThat(BadRequestException.of("bad")).hasMessage("bad");
        }

        @Test
        @DisplayName("carries the caller message for an internal failure")
        void shouldCarryMessageWhenFailureIsInternal() {
            assertThat(InternalException.of("down")).hasMessage("down");
        }

        @Test
        @DisplayName("carries the caller message for an unauthorized call")
        void shouldCarryMessageWhenCallIsUnauthorized() {
            assertThat(UnauthorizedException.of("denied")).hasMessage("denied");
        }

        @Test
        @DisplayName("carries the caller message for a failed precondition")
        void shouldCarryMessageWhenPreconditionFails() {
            assertThat(PreConditionException.of("missing")).hasMessage("missing");
        }

        @Test
        @DisplayName("copies the notification errors")
        void shouldCopyNotificationWhenNotificationFails() {
            var notification = Notification.create(Error.of("name"));

            var exception = new NotificationException("failed", notification);

            assertThat(exception).hasMessage("failed");
            assertThat(exception.getErrors()).containsExactly(Error.of("name"));
        }
    }
}
