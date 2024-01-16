package net.murdos.giftcards.giftcard.secondary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.murdos.giftcards.giftcard.domain.Barcode;
import net.murdos.giftcards.giftcard.domain.EventStore;
import net.murdos.giftcards.giftcard.domain.GiftCardEvent;

public class InMemoryEventStore implements EventStore {

  private final Map<Barcode, List<GiftCardEvent>> events = new HashMap<>();

  @Override
  public void save(GiftCardEvent event) {
    events.putIfAbsent(event.barcode(), new ArrayList<>());
    events.get(event.barcode()).add(event);
  }

  @Override
  public void save(List<GiftCardEvent> events) {
    events.forEach(this::save);
  }

  @Override
  public List<GiftCardEvent> streamOf(Barcode barcode) {
    return events.get(barcode);
  }

  public void reset() {
    events.clear();
  }
}
