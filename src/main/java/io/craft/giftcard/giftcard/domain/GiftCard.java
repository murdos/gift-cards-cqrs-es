package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.*;
import io.craft.giftcard.shared.collection.domain.SequentialCombiner;
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

  public static GiftCardEvent declare(GiftCardDeclaration giftCardDeclaration) {
    return new GiftCardDeclared(
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
        new GifCardExhausted(this.barcode(), paidAmount.sequenceId().next())
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
    SequenceId currentSequenceId,
    Amount remainingAmount
  ) {
    public static DecisionProjection from(GiftCardHistory history) {
      GiftCardDeclared firstEvent = history.start();

      return history
        .followingEvents()
        .stream()
        .reduce(
          new DecisionProjection(
            firstEvent.barcode(),
            firstEvent.sequenceId(),
            firstEvent.amount()
          ),
          DecisionProjection::accumulator,
          new SequentialCombiner<>()
        );
    }

    public DecisionProjection withRemainingAmount(Amount remainingAmount) {
      return new DecisionProjection(barcode, currentSequenceId, remainingAmount);
    }

    public DecisionProjection withSequenceId(SequenceId sequenceId) {
      return new DecisionProjection(barcode, sequenceId, remainingAmount);
    }

    public SequenceId nextSequenceId() {
      return currentSequenceId.next();
    }
    //@formatter:off
    private static DecisionProjection accumulator(
      DecisionProjection currentProjection,
      GiftCardEvent event
    ) {
      return switch (event) {
        case GiftCardDeclared __ -> currentProjection;
        case PaidAmount paidAmount -> currentProjection
          .withRemainingAmount(
            currentProjection.remainingAmount().subtract(paidAmount.amount())
          )
          .withSequenceId(paidAmount.sequenceId());
        case GifCardExhausted gifCardExhausted -> currentProjection.withSequenceId(
          gifCardExhausted.sequenceId()
        );
      };
    }
  } //@formatter:on
  // tag::closingBrace[]
}
// end::closingBrace[]
