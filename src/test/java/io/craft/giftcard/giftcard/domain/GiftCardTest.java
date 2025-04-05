package io.craft.giftcard.giftcard.domain;

import static io.craft.giftcard.giftcard.domain.GiftCardFixtures.paymentDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import io.craft.giftcard.UnitTest;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.*;
import io.craft.giftcard.giftcard.domain.events.GiftCardDeclared;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

@UnitTest
class GiftCardTest {

  @Test
  void shouldDeclareGiftCard() {
    Barcode barcode = GiftCardFixtures.barcode();
    Amount amount = GiftCardFixtures.amount();
    ShoppingStore shoppingStore = ShoppingStore.RESTAURANT;
    GiftCardDeclaration giftCardDeclaration = new GiftCardDeclaration(
      barcode,
      amount,
      shoppingStore
    );

    GiftCardEvent event = GiftCard.declare(giftCardDeclaration);

    assertThat(event).isInstanceOfSatisfying(GiftCardDeclared.class, GiftCardDeclared -> {
      assertThat(GiftCardDeclared.sequenceId()).isEqualTo(new SequenceId(0));
      assertThat(GiftCardDeclared.barcode()).isEqualTo(barcode);
      assertThat(GiftCardDeclared.amount()).isEqualTo(amount);
    });
  }

  @Test
  void shouldPayAmountOnAGivenGiftCard() {
    GiftCardHistory history = givenGiftCardHistory(100);
    var giftcard = new GiftCard(history);

    Amount amountToPay = Amount.of(50);
    List<GiftCardEvent> events = giftcard.pay(new Payment(amountToPay, paymentDate()));

    assertThat(events)
      .singleElement()
      .isInstanceOfSatisfying(PaidAmount.class, paidAmount -> {
        assertThat(paidAmount.sequenceId()).isGreaterThan(history.lastSequenceId());
        assertThat(paidAmount.amount()).isEqualTo(amountToPay);
        assertThat(paidAmount.barcode()).isEqualTo(giftcard.barcode());
      });
  }

  @Test
  void shouldNotAllowToPayMoreThanTheRemainingAmount() {
    GiftCardHistory history = givenGiftCardHistory(100);
    var giftcard = new GiftCard(history);

    Amount amountToPay = Amount.of(150);
    Throwable exception = catchThrowable(() -> giftcard.pay(new Payment(amountToPay, paymentDate()))
    );

    assertThat(exception).isInstanceOf(InsufficientRemainingAmountException.class);
  }

  @Test
  void shouldExhaustAGiftcardWhenRemainingAmountIsZero() {
    GiftCardHistory history = givenGiftCardHistory(100);
    var giftcard = new GiftCard(history);

    Amount amountToPay = Amount.of(100);
    List<GiftCardEvent> events = giftcard.pay(new Payment(amountToPay, paymentDate()));

    assertThat(events)
      .extracting(GiftCardEvent::getClass)
      .containsExactly(PaidAmount.class, GifCardExhausted.class);
    var setOfSequenceIds = events
      .stream()
      .map(GiftCardEvent::sequenceId)
      .collect(Collectors.toSet());
    assertThat(setOfSequenceIds).hasSize(2);
  }

  private static GiftCardHistory givenGiftCardHistory(double initialAmount) {
    Barcode barcode = GiftCardFixtures.barcode();
    Amount amount = Amount.of(initialAmount);
    ShoppingStore shoppingStore = ShoppingStore.RESTAURANT;
    GiftCardDeclaration giftCardDeclaration = new GiftCardDeclaration(
      barcode,
      amount,
      shoppingStore
    );
    var GiftCardDeclared = GiftCard.declare(giftCardDeclaration);

    return new GiftCardHistory((GiftCardDeclared) GiftCardDeclared);
  }
}
