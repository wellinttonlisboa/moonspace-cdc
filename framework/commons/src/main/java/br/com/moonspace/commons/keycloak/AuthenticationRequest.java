package br.com.moonspace.commons.keycloak;

public record AuthenticationRequest(String clientId, String clientSecret) {
}
