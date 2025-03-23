package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.StoredEvent;
import io.craft.giftcard.giftcard.domain.StoredHistory;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.event.annotation.DomainEventHandler;

public class GiftCardCurrentStateUpdater implements EventHandler<GiftCardEvent> {

  private final GiftCardEventStore eventStore;
  private final GiftCardCurrentStateRepository viewRepository;

  public GiftCardCurrentStateUpdater(GiftCardEventStore eventStore, GiftCardCurrentStateRepository viewRepository) {
    this.eventStore = eventStore;
    this.viewRepository = viewRepository;
  }

  @Override
  @DomainEventHandler
  public void handle(StoredEvent<GiftCardEvent> event) {
    StoredHistory history = eventStore.getHistory(event.event().barcode());

    viewRepository.save(GiftCardCurrentState.from(history));
  }
}
