package io.craft.giftcard.giftcard.domain.events;

import io.craft.giftcard.giftcard.domain.Barcode;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GifCardExhausted(Barcode barcode) implements GiftCardEvent {}
