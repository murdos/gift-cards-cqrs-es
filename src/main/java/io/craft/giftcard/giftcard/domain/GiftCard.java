package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.commands.GiftCardCreation;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;

public class GiftCard {
  public static GiftCardEvent create(GiftCardCreation giftCardCreation) {
    return new GiftCardCreated(giftCardCreation.barcode(), giftCardCreation.amount(), SequenceId.INITIAL);
  }
}

