package net.murdos.giftcards.giftcard.domain;

import net.murdos.giftcards.shared.error.domain.Assert;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Barcode(String value) {
  public Barcode {
    Assert.notBlank("value", value);
  }

  public String get() {
    return value();
  }
}
