package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class InMemoryProjectionsInitializer {

  private final GiftCardEventStore eventStore;
  private final EventPublisher<GiftCardEvent> eventPublisher;

  InMemoryProjectionsInitializer(
    GiftCardEventStore eventStore,
    EventPublisher<GiftCardEvent> eventPublisher
  ) {
    this.eventStore = eventStore;
    this.eventPublisher = eventPublisher;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationStarted() {
    eventStore.stream().forEach(eventPublisher::publish);
  }
}
