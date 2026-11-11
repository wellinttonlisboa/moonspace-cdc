package br.com.moonspace.commons.support;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Solitary unit test. Class name is {@code SubjectTest}.
 * Methods are {@code should<Outcome>When<Condition>} and use AssertJ.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag("unit")
public @interface UnitTest {
}
