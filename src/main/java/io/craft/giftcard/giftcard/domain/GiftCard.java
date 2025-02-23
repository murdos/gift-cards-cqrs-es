package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public class GiftCard {

  private final DecisionProjection decisionProjection;

  public GiftCard(GiftCardHistory history) {
    decisionProjection = DecisionProjection.from(history);
  }

  public static GiftCardEvent declare(GiftCardDeclaration giftCardDeclaration) {
    return new GiftCardCreated(giftCardDeclaration.barcode(), giftCardDeclaration.amount(), SequenceId.INITIAL);
  }

  public GiftCardEvent pay(Payment payment) {
    // Amount currentAmount = decisionProjection.amount;
    // assert(currentAmount.subtract(payment.amount()) > 0;
    return new PaidAmount(decisionProjection.barcode, decisionProjection.nextSequenceId(), payment.amount());
  }

  private record DecisionProjection(Barcode barcode, Amount amount, SequenceId currentSequenceId) {
    public static DecisionProjection from(GiftCardHistory history) {
      GiftCardCreated firstEvent = history.start();
      return new DecisionProjection(firstEvent.barcode(), firstEvent.amount(), firstEvent.sequenceId());
    }

    public SequenceId nextSequenceId() {
      return currentSequenceId.next();
    }
  }
}
