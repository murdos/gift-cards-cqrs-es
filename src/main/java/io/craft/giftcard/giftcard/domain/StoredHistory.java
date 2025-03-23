package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import java.util.List;

public record StoredHistory(StoredEvent<GiftCardCreated> start, List<StoredEvent<GiftCardEvent>> followingEvents) {
  public GiftCard toGiftCard() {
    return new GiftCard(new GiftCardHistory(start.event(), followingEvents.stream().map(StoredEvent::event).toList()));
  }

  public SequenceId getSequenceId() {
    return followingEvents.stream().reduce(start.sequenceId(), (lastSequenceId, event) -> event.sequenceId(), new DummyCombiner<>());
  }
}
