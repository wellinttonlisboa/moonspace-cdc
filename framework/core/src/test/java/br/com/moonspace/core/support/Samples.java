package br.com.moonspace.core.support;

import br.com.moonspace.core.architecture.basic.cqrs.commands.DomainCommand;
import br.com.moonspace.core.architecture.basic.cqrs.queries.DomainQuery;
import br.com.moonspace.core.architecture.basic.ddd.AggregateRoot;
import br.com.moonspace.core.architecture.basic.ddd.Identifier;
import br.com.moonspace.core.architecture.basic.ddd.events.DomainEvent;
import br.com.moonspace.core.validation.ValidationHandler;

import java.time.Instant;
import java.util.List;

public final class Samples {

    private Samples() {
    }

    public static final class SampleId extends Identifier {

        private final String value;

        public SampleId(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof SampleId id && value.equals(id.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    public static final class SampleEvent implements DomainEvent {

        @Override
        public Instant occurredOn() {
            return Instant.EPOCH;
        }
    }

    public static final class SampleAggregate extends AggregateRoot<SampleId> {

        public SampleAggregate(SampleId id) {
            super(id);
        }

        public SampleAggregate(SampleId id, List<DomainEvent> events) {
            super(id, events);
        }

        @Override
        public void validate(ValidationHandler handler) {
        }
    }

    public static final class SampleCommand extends DomainCommand {

        public SampleCommand(String id) {
            super(id);
        }
    }

    public static final class SampleQuery extends DomainQuery {
    }
}
