package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.GifCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import java.util.List;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public class GiftCard {

  private final DecisionProjection decisionProjection;

  protected Barcode barcode() {
    return decisionProjection.barcode;
  }

  public GiftCard(GiftCardHistory history) {
    decisionProjection = DecisionProjection.from(history);
  }

  public static GiftCardCreated declare(GiftCardDeclaration giftCardDeclaration) {
    return new GiftCardCreated(giftCardDeclaration.barcode(), giftCardDeclaration.amount(), giftCardDeclaration.shoppingStore());
  }

  public List<GiftCardEvent> pay(Payment payment) {
    if (decisionProjection.remainingAmount.isLessThan(payment.amount())) {
      throw new InsufficientRemainingAmountException(barcode());
    }
    PaidAmount paidAmount = new PaidAmount(decisionProjection.barcode, payment.amount());

    if (decisionProjection.remainingAmount.equals(payment.amount())) {
      return List.of(paidAmount, new GifCardExhausted(decisionProjection.barcode));
    }

    return List.of(paidAmount);
  }

  private record DecisionProjection(Barcode barcode, Amount remainingAmount) {
    public static DecisionProjection from(GiftCardHistory history) {
      GiftCardCreated firstEvent = history.start();
      List<GiftCardEvent> followingEvents = history.followingEvents();

      return followingEvents
        .stream()
        .reduce(new DecisionProjection(firstEvent.barcode(), firstEvent.amount()), DecisionProjection::reducer, new DummyCombiner<>());
    }

    private static DecisionProjection reducer(DecisionProjection decisionProjection, GiftCardEvent giftCardEvent) {
      return switch (giftCardEvent) {
        case PaidAmount paidAmount -> new DecisionProjection(
          decisionProjection.barcode(),
          decisionProjection.remainingAmount().subtract(paidAmount.amount())
        );
        case GiftCardCreated giftCardCreated -> decisionProjection;
        case GifCardExhausted gifCardExhausted -> new DecisionProjection(
          decisionProjection.barcode(),
          decisionProjection.remainingAmount()
        );
      };
    }
  }
}
