package net.murdos.giftcards.giftcard.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GiftCardAdded(Barcode barcode, Amount value) implements GiftCardEvent {}
