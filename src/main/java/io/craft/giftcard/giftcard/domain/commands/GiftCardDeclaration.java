package io.craft.giftcard.giftcard.domain.commands;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.shared.error.domain.Assert;
import org.jmolecules.architecture.cqrs.Command;

@Command
public record GiftCardDeclaration(Barcode barcode, Amount amount) {
  public GiftCardDeclaration {
    Assert.notNull("barcode", barcode);
    Assert.notNull("amount", amount);
  }
}
