package net.murdos.giftcards.giftcard.domain;

import java.math.BigDecimal;
import net.murdos.giftcards.shared.error.domain.Assert;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record Amount(BigDecimal value) {
  public Amount {
    Assert.field("value", value).positive();
  }

  public static Amount of(Integer value) {
    return new Amount(BigDecimal.valueOf(value));
  }

  public Amount minus(Amount otherAmount) {
    return new Amount(this.value.add(otherAmount.value.negate()));
  }

  public boolean isLessThan(Amount otherAmount) {
    return value.compareTo(otherAmount.value) < 0;
  }

  public boolean isEqualTo(Amount otherAmount) {
    return value.compareTo(otherAmount.value) == 0;
  }
}
