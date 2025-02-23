package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;

public record Barcode(String value) {
  public Barcode {
    Assert.notBlank("value", value);
  }
}
