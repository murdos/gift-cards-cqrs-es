package io.craft.giftcard.giftcard.domain.events;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GiftCardCreated(Barcode barcode, Amount amount, ShoppingStore shoppingStore) implements GiftCardEvent {}
