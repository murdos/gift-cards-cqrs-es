package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;
import java.math.BigDecimal;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Amount(BigDecimal value) {
  public Amount {
    Assert.field("value", value).positive();
  }

  public Amount(double value) {
    this(BigDecimal.valueOf(value));
  }

  public Amount subtract(Amount otherAmount) {
    return new Amount(this.value.subtract(otherAmount.value));
  }
}
