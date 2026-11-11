package br.com.moonspace.core.architecture.basic.ddd.exceptions;

import br.com.moonspace.core.validation.Error;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

@Getter
public class DomainException extends NoStacktraceException {

    protected final transient List<Error> errors;

    protected DomainException(String message, List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException of(Error errors) {
        return new DomainException(errors.error(), List.of(errors));
    }

    public static DomainException of(List<Error> errors) {
        return new DomainException(Strings.EMPTY, errors);
    }

}