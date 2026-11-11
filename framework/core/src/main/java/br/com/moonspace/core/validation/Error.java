package br.com.moonspace.core.validation;

public record Error(
        String error
) {

    public static Error of(final String error) {
        return new Error(error);
    }

}