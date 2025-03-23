package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.application.StoredEvent;
import io.craft.giftcard.giftcard.application.StoredHistory;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jmolecules.architecture.hexagonal.Port;

@Port
public interface GiftCardEventStore {
  default List<GiftCardEvent> apply(Barcode barcode, Function<GiftCard, List<GiftCardEvent>> decide) {
    GiftCardHistory history = getHistory(barcode);
    StoredHistory storedHistory = new StoredHistory(history);

    GiftCard giftCard = new GiftCard(history);

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

    save(newStoreEvents);
    return newEvents;
  }

  void save(List<StoredEvent<GiftCardEvent>> newStoreEvents);

  GiftCardHistory getHistory(Barcode barcode);

  default GiftCardEvent init(Barcode barcode, Supplier<GiftCardEvent> initializer) {
    StoredEvent<GiftCardEvent> firstEvent = new StoredEvent<>(initializer.get(), SequenceId.INITIAL);
    save(List.of(firstEvent));
    return firstEvent.event();
  }
}
