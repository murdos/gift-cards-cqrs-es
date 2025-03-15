package io.craft.giftcard.giftcard.infrastructure.secondary;

import io.craft.giftcard.giftcard.domain.EventHandler;
import io.craft.giftcard.giftcard.domain.EventPublisher;
import io.craft.giftcard.giftcard.domain.events.GiftCardEvent;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SimpleEventPublisher implements EventPublisher<GiftCardEvent> {

  private final List<EventHandler<GiftCardEvent>> eventHandlers;

  @SafeVarargs
  public SimpleEventPublisher(EventHandler<GiftCardEvent>... eventHandlers) {
    this.eventHandlers = List.of(eventHandlers);
  }

  @Override
  public void publish(GiftCardEvent event) {
    eventHandlers.forEach(handler -> handler.handle(event));
  }
}
