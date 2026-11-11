package br.com.moonspace.commons.utils;

import java.util.UUID;

public final class GenId {

    /**
     * Generate a new id with random UUID.
     *
     * @return New id form class.
     */
    public static String newId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
