package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardEventStore {
  GiftCard findByBarcode(Barcode barcode);

  void save(GiftCardEvent event);

  GiftCardHistory getHistory(Barcode barcode);
}
