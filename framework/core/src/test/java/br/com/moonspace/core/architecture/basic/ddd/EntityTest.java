package br.com.moonspace.core.architecture.basic.ddd;

import br.com.moonspace.core.architecture.basic.ddd.events.DomainEvent;
import br.com.moonspace.core.architecture.basic.ddd.events.DomainEventPublisher;
import br.com.moonspace.core.support.Samples.SampleAggregate;
import br.com.moonspace.core.support.Samples.SampleEvent;
import br.com.moonspace.core.support.Samples.SampleId;
import br.com.moonspace.core.support.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class EntityTest {

    @Test
    @DisplayName("rejects an entity without an identifier")
    void shouldRejectEntityWhenIdentifierIsNull() {
        assertThatThrownBy(() -> new SampleAggregate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("'id' should not be null");
    }

    @Test
    @DisplayName("starts without events when none are supplied")
    void shouldStartWithoutEventsWhenEventListIsNull() {
        var entity = new SampleAggregate(new SampleId("1"), null);

        assertThat(entity.getDomainEvents()).isEmpty();
        assertThat(entity.getId().getValue()).isEqualTo("1");
    }

    @Test
    @DisplayName("keeps a copy of the events registered at construction")
    void shouldCopyEventsWhenConstructedWithAList() {
        var supplied = new ArrayList<DomainEvent>();
        supplied.add(new SampleEvent());
        var entity = new SampleAggregate(new SampleId("1"), supplied);

        supplied.clear();

        assertThat(entity.getDomainEvents()).hasSize(1);
        assertThatThrownBy(() -> entity.getDomainEvents().add(new SampleEvent()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("ignores a null event")
    void shouldIgnoreEventWhenItIsNull() {
        var entity = new SampleAggregate(new SampleId("1"));

        entity.registerEvent(null);

        assertThat(entity.getDomainEvents()).isEmpty();
    }

    @Test
    @DisplayName("publishes registered events and then clears them")
    void shouldPublishAndClearEventsWhenPublisherIsPresent() {
        var entity = new SampleAggregate(new SampleId("1"));
        var event = new SampleEvent();
        entity.registerEvent(event);
        var published = new ArrayList<DomainEvent>();

        entity.publishDomainEvents(published::add);

        assertThat(published).containsExactly(event);
        assertThat(entity.getDomainEvents()).isEmpty();
    }

    @Test
    @DisplayName("does nothing when there is no publisher")
    void shouldKeepEventsWhenPublisherIsNull() {
        var entity = new SampleAggregate(new SampleId("1"));
        entity.registerEvent(new SampleEvent());

        entity.publishDomainEvents((DomainEventPublisher) null);

        assertThat(entity.getDomainEvents()).hasSize(1);
    }

    @Test
    @DisplayName("is equal to another entity of the same type and identifier")
    void shouldBeEqualWhenIdentifierMatches() {
        var left = new SampleAggregate(new SampleId("1"));
        var right = new SampleAggregate(new SampleId("1"));

        assertThat(left).isEqualTo(left);
        assertThat(left).isEqualTo(right);
        assertThat(left).hasSameHashCodeAs(right);
        assertThat(left).isNotEqualTo(null);
        assertThat(left).isNotEqualTo(new SampleId("1"));
        assertThat(left).isNotEqualTo(new SampleAggregate(new SampleId("2")));
    }
}
