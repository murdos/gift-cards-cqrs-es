package net.murdos.giftcards.giftcard.domain;

import static net.murdos.giftcards.giftcard.domain.GiftCardsFixture.amount;
import static net.murdos.giftcards.giftcard.domain.GiftCardsFixture.barcode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.murdos.giftcards.UnitTest;
import net.murdos.giftcards.giftcard.secondary.InMemoryEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UnitTest
class GiftCardDomainServiceTest {

  private final InMemoryEventStore eventStore = new InMemoryEventStore();
  private final GiftCardDomainService giftCardDomainService = new GiftCardDomainService(eventStore);

  @BeforeEach
  void resetEventStore() {
    eventStore.reset();
  }

  @Test
  void addGiftCardShouldGenerateGiftCardAddedEvent() {
    AddGiftCard giftCard = new AddGiftCard(barcode(), amount());
    giftCardDomainService.addGiftCard(giftCard);

    assertThat(eventStore.streamOf(giftCard.barcode())).contains(new GiftCardAdded(giftCard.barcode(), giftCard.value()));
  }

  @Test
  void payAmountShouldSaveAnAmountPaidEvent() {
    giftCardDomainService.addGiftCard(new AddGiftCard(barcode(), Amount.of(50)));

    PayAmountWithGiftCard command = new PayAmountWithGiftCard(barcode(), Amount.of(30));
    giftCardDomainService.pay(command);

    assertThat(eventStore.streamOf(command.barcode())).contains(new AmountPaid(command.barcode(), command.amount()));
  }

  @Test
  void payAmountShouldForbidNegativeRemainingAmount() {
    giftCardDomainService.addGiftCard(new AddGiftCard(barcode(), Amount.of(20)));

    PayAmountWithGiftCard command = new PayAmountWithGiftCard(barcode(), Amount.of(30));
    assertThatThrownBy(() -> giftCardDomainService.pay(command)).isInstanceOf(InsufficientCreditException.class);

    assertThat(eventStore.streamOf(command.barcode())).doesNotContain(new AmountPaid(command.barcode(), command.amount()));
  }

  @Test
  void payAmountShouldHandleExhaustedCard() {
    giftCardDomainService.addGiftCard(new AddGiftCard(barcode(), Amount.of(20)));

    PayAmountWithGiftCard command = new PayAmountWithGiftCard(barcode(), Amount.of(20));
    giftCardDomainService.pay(command);

    assertThat(eventStore.streamOf(command.barcode()))
      .contains(new AmountPaid(command.barcode(), command.amount()), new GiftCardExhausted(command.barcode()));
  }
}
