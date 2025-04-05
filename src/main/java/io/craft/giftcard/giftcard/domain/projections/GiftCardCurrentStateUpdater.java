package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.events.GiftCardDeclared;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.jmolecules.event.annotation.DomainEventHandler;

public class GiftCardCurrentStateUpdater implements EventHandler<GiftCardEvent> {

  private final GiftCardCurrentStateRepository currentStateRepository;

  public GiftCardCurrentStateUpdater(GiftCardCurrentStateRepository currentStateRepository) {
    this.currentStateRepository = currentStateRepository;
  }

  @Override
  @DomainEventHandler
  public void handle(GiftCardEvent event) {
    GiftCardCurrentState newState =
      switch (event) {
        case GiftCardDeclared firstEvent -> initializeState(firstEvent);
        case GiftCardEvent followingEvent -> updateState(followingEvent);
      };

    currentStateRepository.save(newState);
  }

  private GiftCardCurrentState initializeState(GiftCardDeclared giftCardDeclared) {
    return GiftCardCurrentState.from(giftCardDeclared);
  }

  private GiftCardCurrentState updateState(GiftCardEvent giftCardEvent) {
    GiftCardCurrentState currentState = currentStateRepository
      .get(giftCardEvent.barcode())
      .orElseThrow();
    return currentState.apply(giftCardEvent);
  }
}
