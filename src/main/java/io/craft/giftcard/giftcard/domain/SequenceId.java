package io.craft.giftcard.giftcard.domain;

import io.craft.giftcard.shared.error.domain.Assert;

public record SequenceId(int value) {
  public static final SequenceId INITIAL = new SequenceId(0);

  public SequenceId {
        Assert.field("value", value).positive();
    }
}
