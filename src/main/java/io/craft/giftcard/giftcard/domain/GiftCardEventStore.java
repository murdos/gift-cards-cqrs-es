package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardEventStore {
  default GiftCard findByBarcode(Barcode barcode) {
    return new GiftCard(getHistory(barcode));
  }

  void save(GiftCardEvent event);

  GiftCardHistory getHistory(Barcode barcode);
}
