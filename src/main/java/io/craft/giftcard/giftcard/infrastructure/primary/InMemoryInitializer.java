package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.application.GiftCardApplicationService;
import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.GiftCard;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import java.math.BigDecimal;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InMemoryInitializer {

  private final GiftCardApplicationService giftCardApplicationService;

  public InMemoryInitializer(GiftCardApplicationService giftCardApplicationService) {
    this.giftCardApplicationService = giftCardApplicationService;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationReady() {
    giftCardApplicationService.declare(new GiftCardDeclaration(new Barcode("5678"), new Amount(new BigDecimal(50))));
    giftCardApplicationService.declare(new GiftCardDeclaration(new Barcode("1234"), new Amount(new BigDecimal(44))));
  }
}
