package io.craft.giftcard.giftcard.domain.commands;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.shared.error.domain.Assert;
import java.time.LocalDate;
import org.jmolecules.architecture.cqrs.Command;

@Command
public record Payment(Amount amount, LocalDate date) {
  public Payment {
    Assert.notNull("amount", amount);
    Assert.notNull("date", date);
  }
}
