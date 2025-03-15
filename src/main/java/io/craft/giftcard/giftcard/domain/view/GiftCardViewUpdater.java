package io.craft.giftcard.giftcard.domain.view;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.GiftCardView;
import io.craft.giftcard.giftcard.domain.GiftCardViewRepository;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardHistory;
import org.jmolecules.event.annotation.DomainEventHandler;

public class GiftCardViewUpdater implements EventHandler<GiftCardEvent> {

  private final GiftCardEventStore eventStore;
  private final GiftCardViewRepository viewRepository;

  public GiftCardViewUpdater(GiftCardEventStore eventStore, GiftCardViewRepository viewRepository) {
    this.eventStore = eventStore;
    this.viewRepository = viewRepository;
  }

  @Override
  @DomainEventHandler
  public void handle(GiftCardEvent event) {
    GiftCardHistory history = eventStore.getHistory(event.barcode());

    viewRepository.save(GiftCardView.from(history));
  }
}
