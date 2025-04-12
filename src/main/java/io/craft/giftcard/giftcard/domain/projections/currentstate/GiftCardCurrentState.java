package io.craft.giftcard.giftcard.domain.projections.currentstate;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import io.craft.giftcard.giftcard.domain.events.GiftCardDeclared;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardExhausted;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import org.jmolecules.architecture.cqrs.QueryModel;

@QueryModel
public record GiftCardCurrentState(
  Barcode barcode,
  Amount remainingAmount,
  ShoppingStore shoppingStore,
  boolean exhausted
) {
  public static GiftCardCurrentState from(GiftCardDeclared giftCardDeclared) {
    return new GiftCardCurrentState(
      giftCardDeclared.barcode(),
      giftCardDeclared.amount(),
      giftCardDeclared.shoppingStore(),
      false
    );
  }

  private GiftCardCurrentState withRemainingAmount(Amount remainingAmount) {
    return new GiftCardCurrentState(barcode, remainingAmount, shoppingStore, exhausted);
  }

  private GiftCardCurrentState exhaust() {
    return new GiftCardCurrentState(barcode, remainingAmount, shoppingStore, true);
  }

  public GiftCardCurrentState apply(GiftCardEvent giftCardEvent) {
    return switch (giftCardEvent) {
      case PaidAmount paidAmount -> this.withRemainingAmount(
          remainingAmount.subtract(paidAmount.amount())
        );
      case GiftCardExhausted __ -> this.exhaust();
      case GiftCardDeclared __ -> throw new IllegalStateException(
        "GiftCardDeclared event is not expected as an update event"
      );
    };
  }
}
