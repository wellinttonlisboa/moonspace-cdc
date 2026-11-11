package br.com.moonspace.core.architecture.basic.ddd.exceptions;

public class NoStacktraceException extends RuntimeException {

    public NoStacktraceException(String message) {
        this(message, null);
    }

    public NoStacktraceException(String message, Throwable e) {
        super(message, e, true, false);
    }

}