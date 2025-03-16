package io.craft.giftcard.giftcard.domain;

import org.jmolecules.architecture.hexagonal.Port;
import org.jmolecules.event.annotation.DomainEventPublisher;

@Port
public interface EventPublisher<T> {
  @DomainEventPublisher
  void publish(T event);

  void register(EventHandler<T> eventHandler);
}
