package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.GifCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import io.craft.giftcard.shared.collection.domain.DummyCombiner;
import java.util.List;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public class GiftCard {

  private final DecisionProjection decisionProjection;

  protected Barcode barcode() {
    return decisionProjection.barcode;
  }

  public GiftCard(GiftCardHistory history) { // ...
    decisionProjection = DecisionProjection.from(history);
  }

  public static GiftCardEvent declare(GiftCardDeclaration giftCardDeclaration) {
    return new GiftCardCreated(
      giftCardDeclaration.barcode(),
      SequenceId.INITIAL,
      giftCardDeclaration.amount(),
      giftCardDeclaration.shoppingStore()
    );
  }

  public List<GiftCardEvent> pay(Payment payment) {
    if (this.hasNotEnoughBalance(payment.amount())) {
      throw new InsufficientRemainingAmountException(this.barcode());
    }
    //@formatter:off
    PaidAmount paidAmount = new PaidAmount(
      this.barcode(),
      this.nextSequenceId(),
      payment.amount(),
      payment.date()
    );
    //@formatter:on

    if (this.hasExactBalance(payment.amount())) {
      return List.of(
        paidAmount,
        new GifCardExhausted(decisionProjection.barcode, paidAmount.sequenceId().next())
      );
    }

    return List.of(paidAmount);
  }

  private boolean hasExactBalance(Amount amount) {
    return decisionProjection.remainingAmount.equals(amount);
  }

  private boolean hasNotEnoughBalance(Amount amount) {
    return decisionProjection.remainingAmount.isLessThan(amount);
  }

  private SequenceId nextSequenceId() {
    return decisionProjection.nextSequenceId();
  }

  private record DecisionProjection(
    Barcode barcode,
    Amount remainingAmount,
    SequenceId currentSequenceId
  ) {
    public static DecisionProjection from(GiftCardHistory history) {
      GiftCardCreated firstEvent = history.start();
      List<GiftCardEvent> followingEvents = history.followingEvents();

      return followingEvents
        .stream()
        .reduce(
          new DecisionProjection(
            firstEvent.barcode(),
            firstEvent.amount(),
            firstEvent.sequenceId()
          ),
          DecisionProjection::reducer,
          new DummyCombiner<>()
        );
    }

    public DecisionProjection withRemainingAmount(Amount remainingAmount) {
      return new DecisionProjection(barcode, remainingAmount, currentSequenceId);
    }

    public DecisionProjection withSequenceId(SequenceId sequenceId) {
      return new DecisionProjection(barcode, remainingAmount, sequenceId);
    }

    public SequenceId nextSequenceId() {
      return currentSequenceId.next();
    }

    private static DecisionProjection reducer(
      DecisionProjection decisionProjection,
      GiftCardEvent giftCardEvent
    ) {
      return switch (giftCardEvent) {
        case GiftCardCreated __ -> decisionProjection;
        case PaidAmount paidAmount -> decisionProjection
          .withRemainingAmount(decisionProjection.remainingAmount().subtract(paidAmount.amount()))
          .withSequenceId(paidAmount.sequenceId());
        case GifCardExhausted gifCardExhausted -> decisionProjection.withSequenceId(
          gifCardExhausted.sequenceId()
        );
      };
    }
  }
  // tag::closingBrace[]
}
