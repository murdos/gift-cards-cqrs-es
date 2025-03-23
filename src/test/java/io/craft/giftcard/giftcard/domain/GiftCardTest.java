package io.craft.giftcard.giftcard.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import io.craft.giftcard.UnitTest;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.GifCardExhausted;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class GiftCardTest {

  @Test
  void shouldDeclareGiftCard() {
    Barcode barcode = GiftCardFixtures.barcode();
    Amount amount = GiftCardFixtures.amount();
    ShoppingStore shoppingStore = ShoppingStore.RESTAURANT_PANORAMIX;
    GiftCardDeclaration giftCardDeclaration = new GiftCardDeclaration(barcode, amount, shoppingStore);

    GiftCardEvent event = GiftCard.declare(giftCardDeclaration);

    assertThat(event).isInstanceOfSatisfying(GiftCardCreated.class, giftCardCreated -> {
      assertThat(giftCardCreated.barcode()).isEqualTo(barcode);
      assertThat(giftCardCreated.amount()).isEqualTo(amount);
    });
  }

  @Test
  void shouldPayAmountOnAGivenGiftCard() {
    StoredHistory history = givenGiftCardHistory(100);
    var giftcard = history.toGiftCard();

    Amount amountToPay = new Amount(new BigDecimal(50));
    List<GiftCardEvent> events = giftcard.pay(new Payment(amountToPay));

    assertThat(events)
      .singleElement()
      .isInstanceOfSatisfying(PaidAmount.class, paidAmount -> {
        assertThat(paidAmount.amount()).isEqualTo(amountToPay);
        assertThat(paidAmount.barcode()).isEqualTo(giftcard.barcode());
      });
  }

  @Test
  void shouldNotAllowToPayMoreThanTheRemainingAmount() {
    StoredHistory history = givenGiftCardHistory(100);
    var giftcard = history.toGiftCard();

    Amount amountToPay = new Amount(new BigDecimal(150));
    Throwable exception = catchThrowable(() -> giftcard.pay(new Payment(amountToPay)));

    assertThat(exception).isInstanceOf(InsufficientRemainingAmountException.class);
  }

  @Test
  void shouldExhaustAGiftcardWhenRemainingAmountIsZero() {
    StoredHistory history = givenGiftCardHistory(100);
    var giftcard = history.toGiftCard();

    Amount amountToPay = new Amount(new BigDecimal(100));
    List<GiftCardEvent> events = giftcard.pay(new Payment(amountToPay));

    assertThat(events).extracting(GiftCardEvent::getClass).containsExactly(PaidAmount.class, GifCardExhausted.class);
  }

  private static StoredHistory givenGiftCardHistory(double initialAmount) {
    Barcode barcode = GiftCardFixtures.barcode();
    Amount amount = new Amount(new BigDecimal(initialAmount));
    ShoppingStore shoppingStore = ShoppingStore.RESTAURANT_PANORAMIX;
    GiftCardDeclaration giftCardDeclaration = new GiftCardDeclaration(barcode, amount, shoppingStore);
    var giftCardCreated = GiftCard.declare(giftCardDeclaration);

    return new StoredHistory(new StoredEvent<GiftCardCreated>(giftCardCreated, SequenceId.INITIAL), List.of());
  }
}
