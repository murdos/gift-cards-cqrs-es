package io.craft.giftcard.giftcard.domain;

import java.util.ArrayList;
import java.util.List;
import org.jmolecules.ddd.annotation.Service;
import org.jmolecules.event.annotation.DomainEventPublisher;

@Service
public class EventPublisher<T> {

  private final List<EventHandler<T>> eventHandlers = new ArrayList<>();

  public EventPublisher<T> register(EventHandler<T> eventHandler) {
    eventHandlers.add(eventHandler);
    return this;
  }

  @DomainEventPublisher
  public void publish(T event) {
    eventHandlers.forEach(handler -> handler.handle(event));
  }
}
