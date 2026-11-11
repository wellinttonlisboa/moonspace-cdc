package br.com.moonspace.commons.mongo;

import br.com.moonspace.commons.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@UnitTest
class CascadeCallbackTest {

    @Test
    @DisplayName("saves a single reference and a collection marked for save")
    void shouldSaveReferencesWhenCascadeIsSaveOrAll() {
        var operations = mock(MongoOperations.class);
        var child = new Child("1");
        var other = new Child("2");
        var parent = new Parent();
        parent.all = child;
        parent.saved = List.of(other);

        ReflectionUtils.doWithFields(Parent.class, new CascadeSaveCallback(parent, operations));

        verify(operations).save(child);
        verify(operations).save(other);
    }

    @Test
    @DisplayName("ignores save cascade when the value is missing or delete-only")
    void shouldIgnoreSaveWhenValueIsMissingOrDeleteOnly() {
        var operations = mock(MongoOperations.class);
        var parent = new Parent();
        parent.deleted = new Child("3");

        ReflectionUtils.doWithFields(Parent.class, new CascadeSaveCallback(parent, operations));

        verifyNoInteractions(operations);
    }

    @Test
    @DisplayName("removes a single reference and a collection marked for delete")
    void shouldRemoveReferencesWhenCascadeIsDeleteOrAll() {
        var operations = mock(MongoOperations.class);
        var child = new Child("1");
        var other = new Child("2");
        var parent = new Parent();
        var deleted = new Child("3");
        parent.all = child;
        parent.deleted = deleted;
        parent.deletedMany = List.of(other);

        ReflectionUtils.doWithFields(Parent.class, new CascadeDeleteCallback(parent, operations));

        verify(operations).remove(child);
        verify(operations).remove(deleted);
        verify(operations).remove(other);
    }

    @Test
    @DisplayName("ignores delete cascade when the value is missing or save-only")
    void shouldIgnoreDeleteWhenValueIsMissingOrSaveOnly() {
        var operations = mock(MongoOperations.class);
        var parent = new Parent();
        parent.saved = List.of(new Child("2"));

        ReflectionUtils.doWithFields(Parent.class, new CascadeDeleteCallback(parent, operations));

        verifyNoInteractions(operations);
    }

    @Test
    @DisplayName("detects an identifier and reads the cascade default")
    void shouldDetectIdentifierWhenFieldIsAnnotated() {
        var withId = new IdentifyCallback();
        var withoutId = new IdentifyCallback();
        ReflectionUtils.doWithFields(Child.class, withId);
        ReflectionUtils.doWithFields(Plain.class, withoutId);
        var annotation = Parent.class.getDeclaredFields()[0].getAnnotation(Cascade.class);

        assertThat(annotation.value()).isEqualTo(CascadeType.ALL);
        assertThat(withId.isIdFound()).isTrue();
        assertThat(withoutId.isIdFound()).isFalse();
    }

    private static final class Parent {
        @DBRef
        @Cascade
        private Child all;
        @DBRef
        @Cascade(CascadeType.SAVE)
        private List<Child> saved;
        @DBRef
        @Cascade(CascadeType.DELETE)
        private Child deleted;
        @DBRef
        @Cascade(CascadeType.DELETE)
        private List<Child> deletedMany;
        @DBRef
        private Child referenceOnly;
        @Cascade
        private Child cascadeOnly;
        private String plain;
    }

    private static final class Child {
        @Id
        private final String id;

        private Child(String id) {
            this.id = id;
        }
    }

    private static final class Plain {
        private String name;
    }
}
