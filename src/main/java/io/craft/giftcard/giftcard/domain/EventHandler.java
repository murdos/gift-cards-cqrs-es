package io.craft.giftcard.giftcard.domain;

import org.jmolecules.event.annotation.DomainEventHandler;

public interface EventHandler<T> {
  @DomainEventHandler
  void handle(T event);
}
