package io.craft.giftcard.giftcard.application;

import io.craft.giftcard.giftcard.domain.*;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.springframework.stereotype.Service;

@Service
public class GiftCardApplicationService {

  private final GiftCardEventStore eventStore;
  private final GiftCardViewRepository viewRepository;

  public GiftCardApplicationService(GiftCardEventStore eventStore, GiftCardViewRepository viewRepository) {
    this.eventStore = eventStore;
    this.viewRepository = viewRepository;
  }

  public void declare(GiftCardDeclaration giftCardDeclaration) {
    GiftCardEvent event = GiftCard.declare(giftCardDeclaration);
    eventStore.save(event);

    GiftCardHistory history = eventStore.getHistory(giftCardDeclaration.barcode());

    GiftCardView view = GiftCardView.from(history);
    viewRepository.save(view);
  }

  public GiftCardView findBy(Barcode barcode) {
    return viewRepository.get(barcode);
  }
}
