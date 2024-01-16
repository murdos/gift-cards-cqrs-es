package net.murdos.giftcards.giftcard.domain;

import org.jmolecules.architecture.cqrs.Command;

@Command
public record AddGiftCard(Barcode barcode, Amount value) {}
