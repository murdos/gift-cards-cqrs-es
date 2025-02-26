package io.craft.giftcard.giftcard.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.BarcodeAlreadyUsedException;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.infrastructure.secondary.InMemoryGiftCardEventStore;
import io.craft.giftcard.giftcard.infrastructure.secondary.InMemoryGiftCardViewRepository;
import org.junit.jupiter.api.Test;

class GiftCardApplicationServiceTest {

  private GiftCardApplicationService giftCardApplicationService = new GiftCardApplicationService(
    new InMemoryGiftCardEventStore(),
    new InMemoryGiftCardViewRepository()
  );

  @Test
  void shouldForbidCreationOfGiftcardWithAnExistingBarcode() {
    var barcode = new Barcode("1234567890");
    var amount = new Amount(100);
    var giftCardDeclaration = new GiftCardDeclaration(barcode, amount);
    giftCardApplicationService.declare(giftCardDeclaration);

    Throwable exception = catchThrowable(() -> giftCardApplicationService.declare(giftCardDeclaration));

    assertThat(exception).isInstanceOf(BarcodeAlreadyUsedException.class);
  }
}
