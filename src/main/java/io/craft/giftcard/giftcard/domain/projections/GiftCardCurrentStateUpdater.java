package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
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
  public void handle(GiftCardEvent event) {
    GiftCardHistory history = eventStore.getHistory(event.barcode());

    viewRepository.save(GiftCardCurrentState.from(history));
  }
}
