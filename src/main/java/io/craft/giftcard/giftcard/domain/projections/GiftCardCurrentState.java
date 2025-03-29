package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import io.craft.giftcard.giftcard.domain.events.GifCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import io.craft.giftcard.shared.collection.domain.DummyCombiner;
import java.util.List;
import org.jmolecules.architecture.cqrs.QueryModel;

@QueryModel
public record GiftCardCurrentState(
  Barcode barcode,
  Amount remainingAmount,
  ShoppingStore shoppingStore,
  GiftCardState state
) {
  public static GiftCardCurrentState from(GiftCardHistory history) {
    GiftCardCreated firstEvent = history.start();
    List<GiftCardEvent> followingEvents = history.followingEvents();

    return followingEvents
      .stream()
      .reduce(
        new GiftCardCurrentState(
          firstEvent.barcode(),
          firstEvent.amount(),
          firstEvent.shoppingStore(),
          GiftCardState.ONGOING
        ),
        GiftCardCurrentState::reducer,
        new DummyCombiner<>()
      );
  }

  private GiftCardCurrentState withRemainingAmount(Amount remainingAmount) {
    return new GiftCardCurrentState(barcode(), remainingAmount, shoppingStore(), state());
  }

  private GiftCardCurrentState withState(GiftCardState state) {
    return new GiftCardCurrentState(barcode(), remainingAmount(), shoppingStore(), state);
  }

  private static GiftCardCurrentState reducer(
    GiftCardCurrentState giftCardCurrentState,
    GiftCardEvent giftCardEvent
  ) {
    return switch (giftCardEvent) {
      case PaidAmount paidAmount -> giftCardCurrentState.withRemainingAmount(
        giftCardCurrentState.remainingAmount().subtract(paidAmount.amount())
      );
      case GiftCardCreated __ -> giftCardCurrentState;
      case GifCardExhausted __ -> giftCardCurrentState.withState(GiftCardState.EXHAUSTED);
    };
  }

  public enum GiftCardState {
    ONGOING,
    EXHAUSTED,
  }
}
