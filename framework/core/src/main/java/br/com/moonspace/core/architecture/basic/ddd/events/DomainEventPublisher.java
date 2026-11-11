package br.com.moonspace.core.architecture.basic.ddd.events;

@FunctionalInterface
public interface DomainEventPublisher {
    void publishEvent(DomainEvent event);
}
