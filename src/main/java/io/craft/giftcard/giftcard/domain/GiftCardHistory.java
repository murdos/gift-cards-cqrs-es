package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.util.List;
import java.util.stream.Stream;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record GiftCardHistory(GiftCardCreated start, List<GiftCardEvent> followingEvents) {
  public GiftCardHistory(GiftCardCreated start, GiftCardEvent... followingEvents) {
    this(start, List.of(followingEvents));
  }

  public Stream<GiftCardEvent> historyStream() {
    return Stream.concat(Stream.of(start), followingEvents.stream());
  }

  public SequenceId lastSequenceId() {
    return historyStream().map(GiftCardEvent::sequenceId).max(SequenceId::compareTo).orElseThrow();
  }
}
