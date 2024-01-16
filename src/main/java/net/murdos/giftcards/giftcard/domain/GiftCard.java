package net.murdos.giftcards.giftcard.domain;

import java.util.List;
import org.jmolecules.ddd.annotation.AggregateRoot;

@AggregateRoot
public class GiftCard {

  private final DecisionProjection projection;

  private GiftCard(List<GiftCardEvent> events) {
    this.projection = new DecisionProjection(events);
  }

  public static GiftCard from(List<GiftCardEvent> events) {
    return new GiftCard(events);
  }

  public static List<GiftCardEvent> from(AddGiftCard addGiftCard) {
    return List.of(new GiftCardAdded(addGiftCard.barcode(), addGiftCard.value()));
  }

  public List<GiftCardEvent> pay(PayAmountWithGiftCard payAmountWithGiftCard) {
    if (projection.remainingAmount.isLessThan(payAmountWithGiftCard.amount())) {
      throw new InsufficientCreditException();
    }
    AmountPaid amountPaidEvent = new AmountPaid(payAmountWithGiftCard.barcode(), payAmountWithGiftCard.amount());
    if (projection.remainingAmount.isEqualTo(payAmountWithGiftCard.amount())) {
      return List.of(amountPaidEvent, new GiftCardExhausted(projection.barcode));
    }
    return List.of(amountPaidEvent);
  }

  private static class DecisionProjection {

    private Barcode barcode;
    private Amount remainingAmount;

    DecisionProjection(List<GiftCardEvent> events) {
      events.forEach(this::apply);
    }

    private void apply(GiftCardEvent giftCardEvent) {
      switch (giftCardEvent) {
        case GiftCardAdded giftCardAdded -> apply(giftCardAdded);
        case AmountPaid amountPaid -> apply(amountPaid);
        case GiftCardExhausted __ -> {}
      }
    }

    private void apply(AmountPaid amountPaid) {
      remainingAmount = remainingAmount.minus(amountPaid.amount());
    }

    private void apply(GiftCardAdded giftCardAdded) {
      remainingAmount = giftCardAdded.value();
      barcode = giftCardAdded.barcode();
    }
  }
}
