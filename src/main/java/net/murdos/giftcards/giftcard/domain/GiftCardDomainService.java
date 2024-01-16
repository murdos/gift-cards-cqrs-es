package net.murdos.giftcards.giftcard.domain;

import java.util.Collection;
import java.util.List;
import org.jmolecules.ddd.annotation.Service;

@Service
public class GiftCardDomainService {

  private final EventStore eventStore;

  public GiftCardDomainService(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  public Collection<GiftCardEvent> addGiftCard(AddGiftCard giftCard) {
    List<GiftCardEvent> events = GiftCard.from(giftCard);
    eventStore.save(events);
    return events;
  }

  public List<GiftCardEvent> pay(PayAmountWithGiftCard payAmountWithGiftCard) {
    GiftCard giftCard = GiftCard.from(eventStore.streamOf(payAmountWithGiftCard.barcode()));
    List<GiftCardEvent> events = giftCard.pay(payAmountWithGiftCard);
    eventStore.save(events);
    return events;
  }
}
