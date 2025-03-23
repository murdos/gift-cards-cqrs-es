package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.util.List;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardEventStore {
  void save(List<StoredEvent<GiftCardEvent>> newStoreEvents);

  StoredHistory getHistory(Barcode barcode);
}
