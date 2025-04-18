package io.craft.giftcard.giftcard.application;

import io.craft.giftcard.giftcard.domain.Barcode;
import io.craft.giftcard.giftcard.domain.BarcodeAlreadyUsedException;
import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.GiftCard;
import io.craft.giftcard.giftcard.domain.GiftCardEventStore;
import io.craft.giftcard.giftcard.domain.commands.GiftCardDeclaration;
import io.craft.giftcard.giftcard.domain.commands.Payment;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import io.craft.giftcard.giftcard.domain.projections.GiftCardMessageSender;
import io.craft.giftcard.giftcard.domain.projections.MessageSenderEventHandler;
import io.craft.giftcard.giftcard.domain.projections.currentstate.GiftCardCurrentState;
import io.craft.giftcard.giftcard.domain.projections.currentstate.GiftCardCurrentStateRepository;
import io.craft.giftcard.giftcard.domain.projections.currentstate.GiftCardCurrentStateUpdater;
import io.craft.giftcard.giftcard.domain.projections.details.GiftCardDetails;
import io.craft.giftcard.giftcard.domain.projections.details.GiftCardDetailsRepository;
import io.craft.giftcard.giftcard.domain.projections.details.GiftCardDetailsUpdater;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftCardApplicationService {

  private final GiftCardEventStore eventStore;
  private final GiftCardCurrentStateRepository currentStateRepository;
  private final GiftCardDetailsRepository detailsRepository;
  private final EventPublisher<GiftCardEvent> eventPublisher;

  public GiftCardApplicationService(
    GiftCardEventStore eventStore,
    GiftCardCurrentStateRepository currentStateRepository,
    GiftCardDetailsRepository detailsRepository,
    GiftCardMessageSender giftCardMessageSender
  ) {
    this.eventStore = eventStore;
    this.currentStateRepository = currentStateRepository;
    this.detailsRepository = detailsRepository;

    this.eventPublisher = new EventPublisher<GiftCardEvent>()
      .register(new GiftCardCurrentStateUpdater(currentStateRepository))
      .register(new MessageSenderEventHandler(giftCardMessageSender))
      .register(new GiftCardDetailsUpdater(eventStore, detailsRepository));
  }

  @Transactional
  public void declare(GiftCardDeclaration giftCardDeclaration) {
    currentStateRepository
      .get(giftCardDeclaration.barcode())
      .ifPresent(event -> {
        throw new BarcodeAlreadyUsedException(giftCardDeclaration.barcode());
      });

    GiftCardEvent event = GiftCard.declare(giftCardDeclaration);
    eventStore.save(event);
    eventPublisher.publish(event);
  }

  @Transactional
  public void pay(Barcode barcode, Payment payment) {
    GiftCard giftCard = new GiftCard(eventStore.getHistory(barcode));
    var events = giftCard.pay(payment);
    eventStore.save(events);
    events.forEach(eventPublisher::publish);
  }

  @Transactional(readOnly = true)
  public Optional<GiftCardCurrentState> getCurrentState(Barcode barcode) {
    return currentStateRepository.get(barcode);
  }

  @Transactional(readOnly = true)
  public List<GiftCardCurrentState> findAllCurrentStates() {
    return currentStateRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Optional<GiftCardDetails> getDetails(Barcode barcode) {
    return detailsRepository.get(barcode);
  }
  // tag::closingBrace[]
}
// end::closingBrace[]
