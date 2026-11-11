package br.com.moonspace.commons.mongo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Cascade {

    /**
     * Cascade typed with {@link CascadeType}
     *
     * @return Return a cascade type.
     */
    CascadeType value() default CascadeType.ALL;
}
