package io.craft.giftcard.giftcard.domain.events;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.SequenceId;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record GiftCardDeclared(
  Barcode barcode,
  SequenceId sequenceId,
  Amount amount,
  ShoppingStore shoppingStore
)
  implements GiftCardEvent {}
