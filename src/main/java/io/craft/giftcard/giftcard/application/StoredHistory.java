package io.craft.giftcard.giftcard.application;

import io.craft.giftcard.giftcard.domain.DummyCombiner;
import io.craft.giftcard.giftcard.domain.GiftCard;
import io.craft.giftcard.giftcard.domain.SequenceId;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import java.util.List;

public record StoredHistory(StoredEvent<GiftCardCreated> firstStoredEvent, List<StoredEvent<GiftCardEvent>> remainingStoredEvents) {
  @Deprecated
  public StoredHistory(GiftCardHistory history) {
    this(
      new StoredEvent(history.start(), history.start().sequenceId()),
      history
        .followingEvents()
        .stream()
        .map(giftCardEvent -> new StoredEvent<GiftCardEvent>(giftCardEvent, giftCardEvent.sequenceId()))
        .toList()
    );
  }

  public GiftCard toGiftCard() {
    return new GiftCard(new GiftCardHistory(firstStoredEvent.event(), remainingStoredEvents.stream().map(StoredEvent::event).toList()));
  }

  public SequenceId getSequenceId() {
    return remainingStoredEvents
      .stream()
      .reduce(firstStoredEvent.sequenceId(), (SequenceId acc, StoredEvent event) -> event.sequenceId(), new DummyCombiner<>());
  }
}
