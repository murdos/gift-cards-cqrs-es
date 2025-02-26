package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;
import java.math.BigDecimal;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Amount(BigDecimal value) implements Comparable<Amount> {
  public Amount {
    Assert.field("value", value).positive();
  }

  public Amount subtract(Amount otherAmount) {
    return new Amount(this.value.subtract(otherAmount.value));
  }

  @Override
  public int compareTo(Amount o) {
    return value.compareTo(o.value);
  }

  public boolean isLessThan(Amount otherAmount) {
    return value.compareTo(otherAmount.value) < 0;
  }
}
