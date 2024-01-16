package net.murdos.giftcards.giftcard.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record AmountPaid(Barcode barcode, Amount amount) implements GiftCardEvent {}
