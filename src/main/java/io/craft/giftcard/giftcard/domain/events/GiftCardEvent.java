package io.craft.giftcard.giftcard.domain.events;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.SequenceId;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public sealed interface GiftCardEvent permits GiftCardCreated, PaidAmount, GifCardExhausted {
  SequenceId sequenceId();

  Barcode barcode();
}
