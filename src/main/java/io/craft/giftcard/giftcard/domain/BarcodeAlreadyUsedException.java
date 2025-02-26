package io.craft.giftcard.giftcard.domain;

public class BarcodeAlreadyUsedException extends RuntimeException {

  public BarcodeAlreadyUsedException(Barcode barcode) {
    super("Barcode " + barcode.value() + " already used");
  }
}
