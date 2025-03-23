package io.craft.giftcard.giftcard.domain.events;

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
}
