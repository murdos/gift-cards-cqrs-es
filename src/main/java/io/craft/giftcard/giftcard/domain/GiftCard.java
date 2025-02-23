package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public class GiftCard {

  public static GiftCardEvent declare(GiftCardDeclaration giftCardDeclaration) {
    return new GiftCardCreated(giftCardDeclaration.barcode(), giftCardDeclaration.amount(), SequenceId.INITIAL);
  }
}
