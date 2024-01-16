package net.murdos.giftcards.giftcard.domain;

import static net.murdos.giftcards.giftcard.domain.GiftCardsFixture.amount;
import static net.murdos.giftcards.giftcard.domain.GiftCardsFixture.barcode;
import static org.assertj.core.api.Assertions.assertThat;

import net.murdos.giftcards.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class GiftCardTest {

  @Test
  void addGiftCardShouldGenerateGiftCardAddedEvent() {
    AddGiftCard giftCard = new AddGiftCard(barcode(), amount());
    assertThat(GiftCard.from(giftCard)).containsExactly(new GiftCardAdded(giftCard.barcode(), giftCard.value()));
  }
}
