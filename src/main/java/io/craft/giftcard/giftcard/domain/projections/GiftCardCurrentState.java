package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.DummyCombiner;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import io.craft.giftcard.giftcard.domain.events.GifCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import java.util.List;
import org.jmolecules.architecture.cqrs.QueryModel;

@QueryModel
public record GiftCardCurrentState(Barcode barcode, Amount remaingAmount, ShoppingStore shoppingStore) {
  public static GiftCardCurrentState from(GiftCardHistory history) {
    GiftCardCreated firstEvent = history.start();
    List<GiftCardEvent> followingEvents = history.followingEvents();

    return followingEvents
      .stream()
      .reduce(
        new GiftCardCurrentState(firstEvent.barcode(), firstEvent.amount(), firstEvent.shoppingStore()),
        GiftCardCurrentState::reducer,
        new DummyCombiner<>()
      );
  }

  private static GiftCardCurrentState reducer(GiftCardCurrentState giftCardCurrentState, GiftCardEvent giftCardEvent) {
    return switch (giftCardEvent) {
      case PaidAmount paidAmount -> new GiftCardCurrentState(
        giftCardCurrentState.barcode(),
        giftCardCurrentState.remaingAmount().subtract(paidAmount.amount()),
        giftCardCurrentState.shoppingStore()
      );
      case GiftCardCreated giftCardCreated -> giftCardCurrentState;
      case GifCardExhausted gifCardExhausted -> giftCardCurrentState;
    };
  }
}
