package br.com.moonspace.commons.kafka.models.connect;

import br.com.moonspace.commons.keycloak.AuthenticationRequest;
import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class ConnectModelTest {

    @Test
    @DisplayName("keeps the connect payload and the authentication request")
    void shouldKeepPayloadWhenModelsAreCreated() {
        var source = new Source("connector", "cdc", "driver");
        var payload = new ValuePayload<>("after", "before", source, Operation.UPDATE);
        var message = new MessageValue<>(payload);
        var request = new AuthenticationRequest("client", "secret");

        assertThat(message.payload()).isSameAs(payload);
        assertThat(payload.source()).isEqualTo(source);
        assertThat(payload.operation()).isEqualTo(Operation.UPDATE);
        assertThat(request.clientId()).isEqualTo("client");
        assertThat(request.clientSecret()).isEqualTo("secret");
    }
}
