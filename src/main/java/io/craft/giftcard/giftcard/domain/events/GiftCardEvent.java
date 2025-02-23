package io.craft.giftcard.giftcard.domain.events;

import io.craft.giftcard.giftcard.domain.SequenceId;

public sealed interface GiftCardEvent permits GiftCardCreated {
  SequenceId sequenceId();
}
