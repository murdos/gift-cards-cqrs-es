package net.murdos.giftcards.giftcard.domain;

import java.util.List;
import org.jmolecules.architecture.hexagonal.SecondaryPort;

@SecondaryPort
public interface EventStore {
  void save(GiftCardEvent event);

  void save(List<GiftCardEvent> event);

  List<GiftCardEvent> streamOf(Barcode barcode);
}
