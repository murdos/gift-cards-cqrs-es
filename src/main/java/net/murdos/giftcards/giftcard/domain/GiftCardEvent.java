package net.murdos.giftcards.giftcard.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public sealed interface GiftCardEvent permits AmountPaid, GiftCardAdded, GiftCardExhausted {
  Barcode barcode();
}
