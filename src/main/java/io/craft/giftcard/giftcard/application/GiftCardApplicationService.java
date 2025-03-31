package io.craft.giftcard.giftcard.application;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.BarcodeAlreadyUsedException;
import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.GiftCard;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.GiftCardNotFoundException;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentState;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentStateRepository;
import io.craft.giftcard.giftcard.domain.projections.GiftCardCurrentStateUpdater;
import io.craft.giftcard.giftcard.domain.projections.GiftCardMessageSender;
import io.craft.giftcard.giftcard.domain.projections.MessageSenderEventHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftCardApplicationService {

  private final GiftCardEventStore eventStore;
  private final GiftCardCurrentStateRepository viewRepository;
  private final EventPublisher<GiftCardEvent> eventPublisher;

  public GiftCardApplicationService(
    GiftCardEventStore eventStore,
    GiftCardCurrentStateRepository viewRepository,
    GiftCardMessageSender giftCardMessageSender
  ) {
    this.eventStore = eventStore;
    this.viewRepository = viewRepository;

    this.eventPublisher = new EventPublisher<>();
    this.eventPublisher.register(new GiftCardCurrentStateUpdater(eventStore, viewRepository));
    this.eventPublisher.register(new MessageSenderEventHandler(giftCardMessageSender));
  }

  @Transactional
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

  @Transactional(readOnly = true)
  public GiftCardCurrentState findBy(Barcode barcode) {
    return viewRepository.get(barcode).orElseThrow(() -> new GiftCardNotFoundException(barcode));
  }

  @Transactional
  public void pay(Barcode barcode, Payment payment) {
    GiftCard giftCard = eventStore.findByBarcode(barcode);
    var events = giftCard.pay(payment);
    eventStore.save(events);
    events.forEach(eventPublisher::publish);
  }
}
