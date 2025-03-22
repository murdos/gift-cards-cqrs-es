package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
public record CardReload(Amount amount) {
  public CardReload {
    Assert.notNull("amount", amount);
  }
}
