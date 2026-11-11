package br.com.moonspace.core.validation;

import br.com.moonspace.core.support.UnitTest;
import br.com.moonspace.core.validation.handler.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class ValidatorTest {

    @Test
    @DisplayName("exposes the handler received at construction")
    void shouldExposeHandlerWhenValidatorRuns() {
        var handler = Notification.create();
        var validator = new RecordingValidator(handler);

        validator.validate();

        assertThat(validator.seen()).isSameAs(handler);
        assertThat(handler.firstError()).isEqualTo(Error.of("checked"));
    }

    private static final class RecordingValidator extends Validator {

        private ValidationHandler seen;

        private RecordingValidator(ValidationHandler handler) {
            super(handler);
        }

        @Override
        public void validate() {
            seen = validationHandler();
            seen.append(Error.of("checked"));
        }

        private ValidationHandler seen() {
            return seen;
        }
    }
}
