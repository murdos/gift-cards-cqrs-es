package io.craft.giftcard.giftcard.domain;

public class GiftCardNotFoundException extends RuntimeException {

  public GiftCardNotFoundException(Barcode barcode) {
    super("Gift card with barcode " + barcode + " not found");
  }
}
