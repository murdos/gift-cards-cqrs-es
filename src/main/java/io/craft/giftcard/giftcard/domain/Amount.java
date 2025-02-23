package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;
import org.jmolecules.ddd.annotation.ValueObject;

import java.math.BigDecimal;

@ValueObject
public record Amount(BigDecimal value) {
  public Amount {
    Assert.field("value", value).positive();
  }
}
