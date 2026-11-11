package br.com.moonspace.core.support;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a solitary unit test.
 * <p>
 * Naming follows the Jakarta EE testing guideline used by enterprise Java teams:
 * the class is {@code SubjectTest}, and each method is {@code should<Outcome>When<Condition>}
 * with a {@code @DisplayName} sentence. Structure the body as arrange, act, assert.
 * Assert with AssertJ. Do not boot Spring for domain or use-case tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag("unit")
public @interface UnitTest {
}
