package io.craft.giftcard.giftcard.domain.events;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.SequenceId;
import java.time.LocalDate;
import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record PaidAmount(
  Barcode barcode,
  SequenceId sequenceId,
  Amount amount,
  LocalDate paymentDate
)
  implements GiftCardEvent {}
