package io.craft.giftcard.giftcard.infrastructure.primary;

import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.projections.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class InMemoryProjectionsInitializer {

  private final GiftCardEventStore eventStore;
  private final EventPublisher<GiftCardEvent> eventPublisher;

  InMemoryProjectionsInitializer(
    GiftCardEventStore eventStore,
    GiftCardCurrentStateRepository cardCurrentStateRepository,
    GiftCardDetailsRepository detailsRepository,
    WeeklyStatisticsRepository weeklyStatisticsRepository
  ) {
    this.eventStore = eventStore;
    this.eventPublisher = new EventPublisher<GiftCardEvent>()
      .register(new GiftCardCurrentStateUpdater(cardCurrentStateRepository))
      .register(new GiftCardDetailsUpdater(eventStore, detailsRepository))
      .register(new WeeklyStatisticsUpdater(weeklyStatisticsRepository));
  }

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationStarted() {
    eventStore.stream().forEach(eventPublisher::publish);
  }
}
