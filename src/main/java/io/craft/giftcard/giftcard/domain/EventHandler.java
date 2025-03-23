package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.event.annotation.DomainEventHandler;

public interface EventHandler<T extends GiftCardEvent> {
  @DomainEventHandler
  void handle(StoredEvent<T> event);
}
