package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.CardReloaded;
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

  public static GiftCardEvent declare(GiftCardDeclaration giftCardDeclaration) {
    return new GiftCardCreated(
      giftCardDeclaration.barcode(),
      giftCardDeclaration.amount(),
      giftCardDeclaration.shoppingStore(),
      SequenceId.INITIAL
    );
  }

  public List<GiftCardEvent> pay(Payment payment) {
    if (decisionProjection.remainingAmount.isLessThan(payment.amount())) {
      throw new InsufficientRemainingAmountException(barcode());
    }
    SequenceId sequenceId = decisionProjection.nextSequenceId();
    PaidAmount paidAmount = new PaidAmount(decisionProjection.barcode, sequenceId, payment.amount());

    if (decisionProjection.remainingAmount.equals(payment.amount())) {
      return List.of(paidAmount, new GifCardExhausted(decisionProjection.barcode, sequenceId.next()));
    }

    return List.of(paidAmount);
  }

  public List<GiftCardEvent> reload(CardReload cardReload) {
    return List.of(new CardReloaded(decisionProjection.barcode, decisionProjection.nextSequenceId(), cardReload.amount()));
  }

  private record DecisionProjection(Barcode barcode, Amount remainingAmount, SequenceId currentSequenceId) {
    public static DecisionProjection from(GiftCardHistory history) {
      GiftCardCreated firstEvent = history.start();
      List<GiftCardEvent> followingEvents = history.followingEvents();

      return followingEvents
        .stream()
        .reduce(
          new DecisionProjection(firstEvent.barcode(), firstEvent.amount(), firstEvent.sequenceId()),
          DecisionProjection::reducer,
          new DummyCombiner<>()
        );
    }

    public SequenceId nextSequenceId() {
      return currentSequenceId.next();
    }

    private static DecisionProjection reducer(DecisionProjection decisionProjection, GiftCardEvent giftCardEvent) {
      return switch (giftCardEvent) {
        case PaidAmount paidAmount -> new DecisionProjection(
          decisionProjection.barcode(),
          decisionProjection.remainingAmount().subtract(paidAmount.amount()),
          paidAmount.sequenceId()
        );
        case GiftCardCreated giftCardCreated -> decisionProjection;
        case GifCardExhausted gifCardExhausted -> new DecisionProjection(
          decisionProjection.barcode(),
          decisionProjection.remainingAmount(),
          gifCardExhausted.sequenceId()
        );
        case CardReloaded cardReloaded -> new DecisionProjection(
          decisionProjection.barcode(),
          decisionProjection.remainingAmount().add(cardReloaded.amount()),
          cardReloaded.sequenceId()
        );
      };
    }
  }
}
