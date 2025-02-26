package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Barcode(String value) {
  public Barcode {
    Assert.notBlank("value", value);
  }

  @Override
  public String toString() {
    return value;
  }
}
