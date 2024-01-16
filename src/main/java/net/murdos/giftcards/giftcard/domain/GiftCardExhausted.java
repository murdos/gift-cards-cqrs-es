package net.murdos.giftcards.giftcard.domain;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GiftCardExhausted(Barcode barcode) implements GiftCardEvent {}
