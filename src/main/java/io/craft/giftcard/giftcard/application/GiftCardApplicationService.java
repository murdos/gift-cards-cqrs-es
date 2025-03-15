package io.craft.giftcard.giftcard.application;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.BarcodeAlreadyUsedException;
import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.GiftCard;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.GiftCardNotFoundException;
import io.craft.giftcard.giftcard.domain.GiftCardView;
import io.craft.giftcard.giftcard.domain.GiftCardViewRepository;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import org.springframework.stereotype.Service;

@Service
public class GiftCardApplicationService {

  private final GiftCardEventStore eventStore;
  private final GiftCardViewRepository viewRepository;
  private final EventPublisher<GiftCardEvent> eventPublisher;

  public GiftCardApplicationService(
    GiftCardEventStore eventStore,
    GiftCardViewRepository viewRepository,
    EventPublisher<GiftCardEvent> eventPublisher
  ) {
    this.eventStore = eventStore;
    this.viewRepository = viewRepository;
    this.eventPublisher = eventPublisher;
  }

  public void declare(GiftCardDeclaration giftCardDeclaration) {
    viewRepository
      .get(giftCardDeclaration.barcode())
      .ifPresent(event -> {
        throw new BarcodeAlreadyUsedException(giftCardDeclaration.barcode());
      });

    GiftCardEvent event = GiftCard.declare(giftCardDeclaration);
    eventStore.save(event);
    eventPublisher.publish(event);
  }

  public GiftCardView findBy(Barcode barcode) {
    return viewRepository.get(barcode).orElseThrow(() -> new GiftCardNotFoundException(barcode));
  }

  public void pay(Barcode barcode, Payment payment) {
    var events = eventStore.findByBarcode(barcode).pay(payment);
    eventStore.save(events);
    events.forEach(eventPublisher::publish);
  }
}
