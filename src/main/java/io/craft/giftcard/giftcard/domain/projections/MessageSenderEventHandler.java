package io.craft.giftcard.giftcard.domain.projections;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.StoredEvent;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;

public class MessageSenderEventHandler implements EventHandler<GiftCardEvent> {

  private final GiftCardMessageSender messageSender;

  public MessageSenderEventHandler(GiftCardMessageSender messageSender) {
    this.messageSender = messageSender;
  }

  @Override
  public void handle(StoredEvent<GiftCardEvent> event) {
    messageSender.send(event);
  }
}
