package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import org.jmolecules.architecture.cqrs.QueryModel;

@QueryModel
public record GiftCardView(Barcode barcode, Amount remaingAmount) {
  public static GiftCardView from(GiftCardHistory history) {
    GiftCardCreated firstEvent = history.start();
    return new GiftCardView(firstEvent.barcode(), firstEvent.amount());
  }
}
