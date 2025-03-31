package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentStateRepository;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentStateUpdater;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class InMemoryProjectionsInitializer {

  private final GiftCardEventStore eventStore;
  private final EventPublisher<GiftCardEvent> eventPublisher;

  InMemoryProjectionsInitializer(
    GiftCardEventStore eventStore,
    GiftCardCurrentStateRepository giftCardCurrentStateRepository
  ) {
    this.eventStore = eventStore;
    this.eventPublisher = new EventPublisher<>();
    this.eventPublisher.register(
        new GiftCardCurrentStateUpdater(eventStore, giftCardCurrentStateRepository)
      );
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationStarted() {
    eventStore.stream().forEach(eventPublisher::publish);
  }
}
