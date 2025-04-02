package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.domain.Amount;
import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.SequenceId;
import io.craft.giftcard.giftcard.domain.ShoppingStore;
import io.craft.giftcard.giftcard.domain.events.GiftCardCreated;
import io.craft.giftcard.giftcard.domain.events.PaidAmount;
import java.time.LocalDate;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class InMemoryEventStoreInitializer {

  private final GiftCardEventStore eventStore;

  public InMemoryEventStoreInitializer(GiftCardEventStore eventStore) {
    this.eventStore = eventStore;
  }

  @EventListener(ApplicationStartedEvent.class)
  public void onApplicationReady() {
    eventStore.save(
      new GiftCardCreated(
        new Barcode("5678"),
        SequenceId.INITIAL,
        Amount.of(50),
        ShoppingStore.POISSONNERIE
      )
    );
    eventStore.save(
      new PaidAmount(
        new Barcode("5678"),
        SequenceId.INITIAL.next(),
        Amount.of(10),
        LocalDate.parse("2025-03-20")
      )
    );
    eventStore.save(
      new PaidAmount(
        new Barcode("5678"),
        SequenceId.INITIAL.next().next(),
        Amount.of(20),
        LocalDate.parse("2025-03-24")
      )
    );

    eventStore.save(
      new GiftCardCreated(
        new Barcode("1234"),
        SequenceId.INITIAL,
        Amount.of(64),
        ShoppingStore.RESTAURANT
      )
    );
    eventStore.save(
      new PaidAmount(
        new Barcode("1234"),
        SequenceId.INITIAL.next(),
        Amount.of(20),
        LocalDate.parse("2025-03-21")
      )
    );

    eventStore.save(
      new GiftCardCreated(
        new Barcode("91011"),
        SequenceId.INITIAL,
        Amount.of(100),
        ShoppingStore.RESTAURANT
      )
    );

    eventStore.save(
      new GiftCardCreated(
        new Barcode("121314"),
        SequenceId.INITIAL,
        Amount.of(75),
        ShoppingStore.FORGE
      )
    );
  }
}
