package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import java.util.List;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardEventStore {
  default GiftCard findByBarcode(Barcode barcode) {
    return new GiftCard(getHistory(barcode));
  }

  void save(GiftCardEvent event);

  void save(List<GiftCardEvent> events);

  GiftCardHistory getHistory(Barcode barcode);
}
