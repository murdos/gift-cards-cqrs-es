package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.StoredEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardMessageSender {
  void send(StoredEvent<GiftCardEvent> event);
}
