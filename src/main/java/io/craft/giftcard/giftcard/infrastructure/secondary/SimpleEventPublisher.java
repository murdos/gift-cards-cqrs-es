package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SimpleEventPublisher implements EventPublisher<GiftCardEvent> {

  private final List<EventHandler<GiftCardEvent>> eventHandlers = new ArrayList<>();

  @Override
  public void register(EventHandler<GiftCardEvent> eventHandler) {
    eventHandlers.add(eventHandler);
  }

  @Override
  public void publish(GiftCardEvent event) {
    eventHandlers.forEach(handler -> handler.handle(event));
  }
}
