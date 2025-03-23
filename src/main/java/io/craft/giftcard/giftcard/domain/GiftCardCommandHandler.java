package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jmolecules.ddd.annotation.Service;

@Service
public class GiftCardCommandHandler {

  private final GiftCardEventStore eventStore;

  public GiftCardCommandHandler(GiftCardEventStore eventStore) {
    this.eventStore = eventStore;
  }

  public StoredEvent<GiftCardEvent> init(Barcode barcode, Supplier<GiftCardEvent> initializer) {
    StoredEvent<GiftCardEvent> firstEvent = new StoredEvent<>(initializer.get(), SequenceId.INITIAL);
    eventStore.save(List.of(firstEvent));
    return firstEvent;
  }

  public List<StoredEvent<GiftCardEvent>> apply(Barcode barcode, Function<GiftCard, List<GiftCardEvent>> decide) {
    StoredHistory storedHistory = eventStore.getHistory(barcode);

    GiftCard giftCard = storedHistory.toGiftCard();

    List<GiftCardEvent> newEvents = decide.apply(giftCard);

    List<StoredEvent<GiftCardEvent>> newStoreEvents = newEvents
      .stream()
      .collect(
        ArrayList::new,
        (list, event) -> {
          if (list.isEmpty()) {
            list.add(new StoredEvent<>(event, storedHistory.getSequenceId().next()));
          } else {
            SequenceId sequenceId = list.getLast().sequenceId();
            list.add(new StoredEvent<>(event, sequenceId.next()));
          }
        },
        List::addAll
      );

    eventStore.save(newStoreEvents);
    return newStoreEvents;
  }
}
