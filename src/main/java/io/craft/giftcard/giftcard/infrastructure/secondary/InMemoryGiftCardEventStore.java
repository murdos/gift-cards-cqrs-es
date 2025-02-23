package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.GiftCard;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.GiftCardHistory;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jmolecules.architecture.hexagonal.Adapter;
import org.springframework.stereotype.Component;

@Adapter
@Component
public class InMemoryGiftCardEventStore implements GiftCardEventStore {

  private final Map<Barcode, List<GiftCardEvent>> histories = new HashMap<>();

  @Override
  public GiftCard findByBarcode(Barcode barcode) {
    return null;
  }

  @Override
  public void save(GiftCardEvent event) {
    histories.computeIfAbsent(event.barcode(), key -> new ArrayList<>()).add(event);
  }

  @Override
  public GiftCardHistory getHistory(Barcode barcode) {
    var events = histories.get(barcode);

    return new GiftCardHistory((GiftCardCreated) events.getFirst());
  }
}
