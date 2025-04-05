package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;
import java.math.BigDecimal;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Amount(BigDecimal value) implements Comparable<Amount> {
  public static final Amount ZERO = new Amount(BigDecimal.ZERO);

  public Amount {
    Assert.field("value", value).positive();
  }

  public static Amount of(double value) {
    return new Amount(BigDecimal.valueOf(value));
  }

  public Amount subtract(Amount otherAmount) {
    return new Amount(this.value.subtract(otherAmount.value));
  }

  public Amount plus(Amount otherAmount) {
    return new Amount(this.value.add(otherAmount.value));
  }

  @Override
  public int compareTo(Amount o) {
    return value.compareTo(o.value);
  }

  public boolean isLessThan(Amount otherAmount) {
    return value.compareTo(otherAmount.value) < 0;
  }
}
