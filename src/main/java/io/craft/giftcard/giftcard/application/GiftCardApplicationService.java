package io.craft.giftcard.giftcard.application;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.BarcodeAlreadyUsedException;
import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.GiftCard;
import io.craft.giftcard.giftcard.domain.GiftCardCommandHandler;
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
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GiftCardApplicationService {

  private final GiftCardCommandHandler commandHandler;
  private final GiftCardCurrentStateRepository viewRepository;
  private final EventPublisher<GiftCardEvent> eventPublisher;

  public GiftCardApplicationService(
    GiftCardEventStore eventStore,
    GiftCardCurrentStateRepository viewRepository,
    EventPublisher<GiftCardEvent> eventPublisher,
    GiftCardMessageSender giftCardMessageSender
  ) {
    this.commandHandler = new GiftCardCommandHandler(eventStore);
    this.viewRepository = viewRepository;
    this.eventPublisher = eventPublisher;

    this.eventPublisher.register(new GiftCardCurrentStateUpdater(eventStore, viewRepository));
    this.eventPublisher.register(new MessageSenderEventHandler(giftCardMessageSender));
  }

  public void declare(GiftCardDeclaration giftCardDeclaration) {
    viewRepository
      .get(giftCardDeclaration.barcode())
      .ifPresent(event -> {
        throw new BarcodeAlreadyUsedException(giftCardDeclaration.barcode());
      });

    GiftCardEvent firstEvent = commandHandler.init(giftCardDeclaration.barcode(), () -> GiftCard.declare(giftCardDeclaration));

    eventPublisher.publish(firstEvent);
  }

  public GiftCardCurrentState findBy(Barcode barcode) {
    return viewRepository.get(barcode).orElseThrow(() -> new GiftCardNotFoundException(barcode));
  }

  public void pay(Barcode barcode, Payment payment) {
    List<GiftCardEvent> events = commandHandler.apply(barcode, giftCard -> giftCard.pay(payment));
    events.forEach(eventPublisher::publish);
  }
}
