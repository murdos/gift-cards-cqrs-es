package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import java.util.List;
import org.jmolecules.architecture.cqrs.QueryModel;

@QueryModel
public record GiftCardView(Barcode barcode, Amount remaingAmount) {
  public static GiftCardView from(GiftCardHistory history) {
    GiftCardCreated firstEvent = history.start();
    List<GiftCardEvent> followingEvents = history.followingEvents();

    return followingEvents
      .stream()
      .reduce(
        new GiftCardView(firstEvent.barcode(), firstEvent.amount()),
        (giftCardView, giftCardEvent) ->
          switch (giftCardEvent) {
            case PaidAmount paidAmount -> new GiftCardView(
              giftCardView.barcode(),
              giftCardView.remaingAmount().subtract(paidAmount.amount())
            );
            default -> giftCardView;
          },
        (giftCardView1, giftCardView2) -> giftCardView2
      );
  }
}
