package io.craft.giftcard.giftcard.domain.commands;

import io.craft.giftcard.giftcard.domain.Amount;
import org.jmolecules.architecture.cqrs.Command;

@Command
public record Payment(Amount amount) {}
