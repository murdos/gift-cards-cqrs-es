package io.craft.giftcard.giftcard.domain.projections.details;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.events.GiftCardDeclared;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import io.craft.giftcard.shared.collection.domain.SequentialCombiner;
import java.util.ArrayList;
import java.util.List;
import org.jmolecules.architecture.cqrs.QueryModel;

@QueryModel
public record GiftCardDetails(Barcode barcode, List<String> details) {
  public static GiftCardDetails from(GiftCardHistory history) {
    GiftCardDeclared firstEvent = history.start();
    List<GiftCardEvent> followingEvents = history.followingEvents();

    return followingEvents
      .stream()
      .reduce(
        new GiftCardDetails(
          firstEvent.barcode(),
          List.of(
            "Nouvelle carte cadeau : " +
            firstEvent.amount().value() +
            " €" +
            " chez " +
            firstEvent.shoppingStore()
          )
        ),
        GiftCardDetails::reducer,
        new SequentialCombiner<>()
      );
  }

  private GiftCardDetails withDetail(String detail) {
    var details = new ArrayList<>(List.copyOf(this.details));
    details.add(detail);
    return new GiftCardDetails(this.barcode, details);
  }

  private static GiftCardDetails reducer(
    GiftCardDetails giftCardDetails,
    GiftCardEvent giftCardEvent
  ) {
    return switch (giftCardEvent) {
      case PaidAmount paidAmount -> giftCardDetails.withDetail(
        "Montant payé : " + paidAmount.amount().value() + " €, le " + paidAmount.on()
      );
      case GiftCardDeclared __ -> giftCardDetails;
      case GiftCardExhausted __ -> giftCardDetails.withDetail("Carte cadeau épuisée");
    };
  }
}
