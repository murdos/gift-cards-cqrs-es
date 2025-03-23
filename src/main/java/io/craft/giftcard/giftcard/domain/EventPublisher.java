package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.architecture.hexagonal.Port;
import org.jmolecules.event.annotation.DomainEventPublisher;

@Port
public interface EventPublisher<T extends GiftCardEvent> {
  @DomainEventPublisher
  void publish(StoredEvent<T> event);

  void register(EventHandler<T> eventHandler);
}
