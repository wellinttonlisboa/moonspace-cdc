package br.com.moonspace.commons.mongo;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Getter
public class IdentifyCallback implements ReflectionUtils.FieldCallback {

    private boolean idFound;

    /**
     * The Method verifies if a class contains the identifier.
     *
     * @param field Field used with identifier annotation.
     * @throws IllegalArgumentException Argument cannot be verified with cascade.
     * @throws IllegalAccessException Argument cannot be accessed.
     */
    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);

        if (field.isAnnotationPresent(Id.class)) {
            idFound = true;
        }
    }

}