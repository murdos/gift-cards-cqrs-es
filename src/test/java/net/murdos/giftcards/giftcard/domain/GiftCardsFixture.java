package net.murdos.giftcards.giftcard.domain;

import java.math.BigDecimal;

public final class GiftCardsFixture {

  private GiftCardsFixture() {}

  public static Barcode barcode() {
    return new Barcode("0799439112766");
  }

  public static Amount amount() {
    return new Amount(BigDecimal.valueOf(50));
  }

  public static GiftCard giftCard() {
    return null;
  }
}
