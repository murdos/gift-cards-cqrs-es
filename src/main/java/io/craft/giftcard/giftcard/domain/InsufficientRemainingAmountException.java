package io.craft.giftcard.giftcard.domain;

public class InsufficientRemainingAmountException extends RuntimeException {

  public InsufficientRemainingAmountException(Barcode barcode) {
    super("Insufficient remaining amount for gift card with barcode " + barcode);
  }
}
