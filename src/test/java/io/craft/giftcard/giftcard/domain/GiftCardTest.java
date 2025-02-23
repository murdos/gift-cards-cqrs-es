package io.craft.giftcard.giftcard.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.craft.giftcard.giftcard.domain.commands.GiftCardCreation;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class GiftCardTest {

  @Test
  void shouldCreateGiftCard() {
    Barcode barcode = new Barcode("1234567890");
    Amount amount = new Amount(new BigDecimal(100));
    GiftCardCreation giftCardCreation = new GiftCardCreation(barcode, amount);

    GiftCardEvent event = GiftCard.create(giftCardCreation);

    assertThat(event).isInstanceOfSatisfying(GiftCardCreated.class, giftCardCreated -> {
      assertThat(giftCardCreated.sequenceId()).isEqualTo(new SequenceId(0));
      assertThat(giftCardCreated.barcode()).isEqualTo(barcode);
      assertThat(giftCardCreated.amount()).isEqualTo(amount);
    });
  }
}
