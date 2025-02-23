package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;

import java.math.BigDecimal;

public record Amount(BigDecimal value) {
  public Amount {
    Assert.field("value", value).positive();
  }
}
