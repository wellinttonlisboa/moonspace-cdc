package br.com.moonspace.commons.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
public class CascadeDeleteCallback implements ReflectionUtils.FieldCallback {

    private final Object source;
    private final MongoOperations operations;

    /**
     * Method overloading for create cascade on delete callback.
     *
     * @param field Field from entity annotated.
     * @throws IllegalArgumentException Argument cannot be verified with cascade.
     * @throws IllegalAccessException Argument cannot be accessed.
     */
    @Override
    public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);
        if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(Cascade.class)) {
            final Object fieldValue = field.get(source);
            if (Objects.nonNull(fieldValue)) {
                final var callback = new IdentifyCallback();
                final CascadeType cascadeType = field.getAnnotation(Cascade.class).value();
                if (cascadeType.equals(CascadeType.DELETE) || cascadeType.equals(CascadeType.ALL)) {
                    if (fieldValue instanceof Collection<?>) {
                        ((Collection<?>) fieldValue).forEach(operations::remove);
                    } else {
                        ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
                        operations.remove(fieldValue);
                    }
                }
            }
        }
    }
}